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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.adapters.AdapterOptionsInfo;
import de.badgersburrow.derailer.databinding.ActivityMainBinding;
import de.badgersburrow.derailer.databinding.FragmentGameBinding;
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

    FragmentGameBinding binding;
    ArrayList<PlayerSelection> playerSelection;
    private boolean dialogIsActive = false;
    private GameView theGameView;

    Runnable notificationRun;
    Thread notificationThread;

    int connections = 4;

    public static AnimationPath animPath;

    ArrayList<String> options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGameBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        //View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        Bundle b = getArguments();
        playerSelection = (ArrayList<PlayerSelection>) b.getSerializable("Players");
        options = (ArrayList<String>) b.getStringArrayList("Options");
        if (options.contains(Keys.option_connections_4)){
            connections = 4;
        } else if (options.contains(Keys.option_connections_8)){
            connections = 8;
        }
        animPath = new AnimationPath(connections);

        int selectedThemeId = getAct().SP.getInt("theme",0);

        theGameView = new GameView(this, playerSelection, options, connections, new GameTheme(getContext(), selectedThemeId));
        binding.rlMain.addView(theGameView,0);
        //find all notification views
        // put into separate custom view

        showNotification(theGameView.playerSprites.get(0));

        binding.rvBottomInfo.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvBottomInfo.setAdapter(new AdapterOptionsInfo(options));


        binding.gbBack.setOnClickListener(view -> {
            getAct().onBackPressed();
        });
        binding.gbBack.setSoundListener(getAct());

        binding.gbHorn.setOnClickListener(view -> {
            getAct().playSoundHorn(selectedThemeId);
        });
        binding.gbHorn.setSoundListener(getAct());

        binding.btPlay.setDrawableDisabled(R.drawable.button_play_state01);
        binding.btPlay.setDrawablePressed(R.drawable.button_play_state03);
        binding.btPlay.setDrawableNormal(R.drawable.button_play_state02);
        binding.btPlay.setTypeface(getAct().customtf_normal);

        theGameView.setPlayButton(binding.btPlay);

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
                binding.ivNBackground.setColorFilter(playerSprite.getColor(), PorterDuff.Mode.SRC_IN);
                binding.ivNPlayer.setImageBitmap(playerSprite.getBmpMain());
                binding.ivNPlayerColor.setImageBitmap(playerSprite.getBmpColor());
                binding.ivNPlayerColor.setColorFilter(playerSprite.getColor(), PorterDuff.Mode.SRC_IN);
                binding.tvNTexttop.setTypeface(getAct().customtf_normal);
                binding.tvNTextbig.setTypeface(getAct().customtf_normal);
                binding.tvNTextbottom.setTypeface(getAct().customtf_normal);
                binding.tvNTextbig.setText(playerSprite.getLabel(getContext()));
                binding.rlNotification.setVisibility(View.VISIBLE);

                if (playerSprite.isAlive()){
                    String gp = theGameView.gamePhase;
                    if (gp.equals(Keys.gpStart)) {
                        binding.tvNTexttop.setVisibility(View.GONE);
                        binding.tvNTextbottom.setText(R.string.choose_your_start_spot);
                    } else if (gp.equals(Keys.gpPlaying)) {
                        binding.tvNTexttop.setText(R.string.on_to);
                        binding.tvNTextbottom.setText(R.string.place_a_tile);
                    }
                } else {
                    binding.tvNTexttop.setText(R.string.kaboom);
                    binding.tvNTextbottom.setText(R.string.is_out);
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
        return binding.rlNotification != null && binding.rlNotification.getVisibility() == View.VISIBLE;
    }

    public void hideNotification(){
        binding.rlNotification.setVisibility(View.INVISIBLE);
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
        theGameView.pauseThread();
        super.onPause();
    }
    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }
}
