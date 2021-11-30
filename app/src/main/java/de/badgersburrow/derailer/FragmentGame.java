package de.badgersburrow.derailer;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.content.DialogInterface.OnDismissListener;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import de.badgersburrow.derailer.objects.AnimationPath;
import de.badgersburrow.derailer.objects.GameTheme;
import de.badgersburrow.derailer.sprites.PlayerSprite;
import de.badgersburrow.derailer.views.GameButton;
import de.badgersburrow.derailer.views.GameTextButton;
import de.badgersburrow.derailer.objects.PlayerResult;
import de.badgersburrow.derailer.objects.PlayerSelection;

import java.util.ArrayList;

/**
 * Created by cetty on 27.07.16.
 */
public class FragmentGame extends Fragment implements OnClickListener{

    ArrayList<PlayerSelection> playerSelection;
    int connections;
    private boolean dialogIsActive = false;
    private GameView theGameView;

    Runnable notificationRun;
    Thread notificationThread;


    public static AnimationPath animPath;

    RelativeLayout rl_notification;
    ImageView iv_n_background, iv_n_player_color, iv_n_player;
    TextView tv_n_texttop, tv_n_textbig, tv_n_textbottom;

    ArrayList<String> options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        Bundle b = getArguments();
        playerSelection = (ArrayList<PlayerSelection>) b.getSerializable("Players");
        options = (ArrayList<String>) b.getStringArrayList("Options");
        connections = b.getInt("connections",4);
        animPath = new AnimationPath(connections);

        int selectedThemeId = getAct().SP.getInt("theme",0);

        RelativeLayout rl_main = rootView.findViewById(R.id.rl_main);
        theGameView = new GameView(this, playerSelection, options, connections, new GameTheme(getContext(), selectedThemeId));
        rl_main.addView(theGameView,0);
        //find all notification views
        // put into separate custom view
        rl_notification = rootView.findViewById(R.id.rl_notification);
        iv_n_background = rootView.findViewById(R.id.iv_n_background);
        iv_n_player_color = rootView.findViewById(R.id.iv_n_player_color);
        iv_n_player = rootView.findViewById(R.id.iv_n_player);
        tv_n_texttop = rootView.findViewById(R.id.tv_n_texttop);
        tv_n_textbig = rootView.findViewById(R.id.tv_n_textbig);
        tv_n_textbottom = rootView.findViewById(R.id.tv_n_textbottom);

        showNotification(theGameView.playerSprites.get(0));

        GameButton gb_back = rootView.findViewById(R.id.gb_back);
        gb_back.setOnClickListener(view -> {
            getAct().onBackPressed();
        });
        gb_back.setSoundListener(getAct());


        GameTextButton bt_play = rootView.findViewById(R.id.bt_play);
        bt_play.setDrawableDisabled(R.drawable.button_play_state01);
        bt_play.setDrawablePressed(R.drawable.button_play_state03);
        bt_play.setDrawableNormal(R.drawable.button_play_state02);
        bt_play.setTypeface(getAct().customtf_normal);

        theGameView.setPlayButton(bt_play);



        return rootView;
    }


    public void showNotification(final PlayerSprite playerSprite){
        if (notificationThread!=null &&notificationThread.isAlive()){
            notificationThread.interrupt();
            getAct().runOnUiThread(() -> hideNotification());
        }

        getAct().runOnUiThread(notificationRun = () -> {
            if (getContext() == null){
                return;
            }

            if (!playerSprite.isVirtual()){
                iv_n_background.setColorFilter(playerSprite.getColor(), PorterDuff.Mode.SRC_IN);
                iv_n_player.setImageBitmap(playerSprite.getBmpMain());
                iv_n_player_color.setImageBitmap(playerSprite.getBmpColor());
                iv_n_player_color.setColorFilter(playerSprite.getColor(), PorterDuff.Mode.SRC_IN);
                tv_n_texttop.setTypeface(getAct().customtf_normal);
                tv_n_textbig.setTypeface(getAct().customtf_normal);
                tv_n_textbottom.setTypeface(getAct().customtf_normal);
                tv_n_textbig.setText(playerSprite.getLabel(getContext()));
                rl_notification.setVisibility(View.VISIBLE);

                if (playerSprite.isAlive()){
                    String gp = theGameView.gamePhase;
                    if (gp.equals(Keys.gpStart)) {
                        tv_n_texttop.setVisibility(View.GONE);
                        tv_n_textbottom.setText(R.string.choose_your_start_spot);
                    } else if (gp.equals(Keys.gpPlaying)) {
                        tv_n_texttop.setText(R.string.on_to);
                        tv_n_textbottom.setText(R.string.place_a_tile);
                    }
                } else {
                    tv_n_texttop.setText(R.string.kaboom);
                    tv_n_textbottom.setText(R.string.is_out);
                }
            }
        });


        notificationThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1000);
                        if (getAct() != null){
                            getAct().runOnUiThread(() -> hideNotification());
                        }
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        notificationThread.start();
    }

    public boolean isNotification(){
        return rl_notification != null && rl_notification.getVisibility() == View.VISIBLE;
    }

    public void hideNotification(){
        rl_notification.setVisibility(View.INVISIBLE);
    }


    public void onGameOver() {
        /*Intent theNextIntent = new Intent(getApplicationContext(),
                FragmentGameOver.class);*/
        ArrayList<PlayerResult> playerResult = new ArrayList<>();
        for (PlayerSprite p: theGameView.playerSprites){
            playerResult.add(p.getResult());
        }
        /*
        theNextIntent.putExtra("PlayersSelection", playerSelection);
        theNextIntent.putExtra("Players", playerResult);
        theNextIntent.putExtra("Options", options);
        startActivity(theNextIntent);
        this.finish();*/

        Bundle b = new Bundle();
        b.putSerializable("PlayersSelection", playerSelection);
        b.putSerializable("Players", playerResult);
        b.putStringArrayList("Options", options);
        b.putInt("connections", connections);
        getAct().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getAct().showGameOver(b);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gb_back:
                getAct().onBackPressed();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        theGameView.resumeThread();
    }


    @Override
    public void onPause(){
        super.onPause();
        theGameView.pauseThread();
    }
    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }
}
