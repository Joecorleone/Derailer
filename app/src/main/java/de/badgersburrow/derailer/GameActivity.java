package de.badgersburrow.derailer;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.content.DialogInterface.OnDismissListener;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.badgersburrow.derailer.objects.AnimationPath;
import de.badgersburrow.derailer.objects.GameTheme;
import de.badgersburrow.derailer.sprites.PlayerSprite;
import de.badgersburrow.derailer.views.GameTextButton;
import de.badgersburrow.derailer.objects.PlayerResult;
import de.badgersburrow.derailer.objects.PlayerSelection;

import java.util.ArrayList;

/**
 * Created by cetty on 27.07.16.
 */
public class GameActivity extends Activity  implements OnClickListener, OnDismissListener{

    ArrayList<PlayerSelection> playerSelection;
    int connections;
    SharedPreferences SP;
    private Dialog gameOverDialog;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.FullScreencall(this);

        playerSelection = (ArrayList<PlayerSelection>) this.getIntent().getExtras().getSerializable("Players");
        options = (ArrayList<String>) this.getIntent().getSerializableExtra("Options");
        connections = this.getIntent().getIntExtra("connections",4);
        animPath = new AnimationPath(connections);

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedThemeId = SP.getInt("theme",0);
        setContentView(R.layout.activity_game);
        RelativeLayout rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        theGameView = new GameView(this, playerSelection, options, connections, new GameTheme(this, selectedThemeId));
        rl_main.addView(theGameView,0);
        //find all notification views
        // put into separate custom view
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        iv_n_background = (ImageView) findViewById(R.id.iv_n_background);
        iv_n_player_color = (ImageView) findViewById(R.id.iv_n_player_color);
        iv_n_player = (ImageView) findViewById(R.id.iv_n_player);
        tv_n_texttop = (TextView) findViewById(R.id.tv_n_texttop);
        tv_n_textbig = (TextView) findViewById(R.id.tv_n_textbig);
        tv_n_textbottom = (TextView) findViewById(R.id.tv_n_textbottom);

        showNotification(theGameView.playerSprites.get(0));

        Button bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(view -> finish());

        GameTextButton bt_play = (GameTextButton) findViewById(R.id.bt_play);
        bt_play.setDrawableDisabled(R.drawable.button_play_state01);
        bt_play.setDrawablePressed(R.drawable.button_play_state03);
        bt_play.setDrawableNormal(R.drawable.button_play_state02);
        bt_play.setTypeface(MainActivity.customtf_normal);

        theGameView.setPlayButton(bt_play);
    }


    public void showNotification(final PlayerSprite playerSprite){
        if (notificationThread!=null &&notificationThread.isAlive()){
            notificationThread.interrupt();
            runOnUiThread(() -> hideNotification());
        }

        runOnUiThread(notificationRun = () -> {

            if (!playerSprite.isVirtual()){
                iv_n_background.setColorFilter(playerSprite.getColor(), PorterDuff.Mode.SRC_IN);
                iv_n_player.setImageBitmap(playerSprite.getBmpMain());
                iv_n_player_color.setImageBitmap(playerSprite.getBmpColor());
                iv_n_player_color.setColorFilter(playerSprite.getColor(), PorterDuff.Mode.SRC_IN);
                tv_n_texttop.setTypeface(MainActivity.customtf_normal);
                tv_n_textbig.setTypeface(MainActivity.customtf_normal);
                tv_n_textbottom.setTypeface(MainActivity.customtf_normal);
                tv_n_textbig.setText(playerSprite.getLabel(getBaseContext()));
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
                        runOnUiThread(() -> hideNotification());
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
        Intent theNextIntent = new Intent(getApplicationContext(),
                GameOverActivity.class);
        ArrayList<PlayerResult> playerResult = new ArrayList<>();
        for (PlayerSprite p: theGameView.playerSprites){
            playerResult.add(p.getResult());
        }
        theNextIntent.putExtra("PlayersSelection", playerSelection);
        theNextIntent.putExtra("Players", playerResult);
        theNextIntent.putExtra("Options", options);
        startActivity(theNextIntent);
        this.finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bMainMenu:
                gameOverDialog.dismiss();
                Intent menuIntent = new Intent(getApplicationContext(),
                        StartMenuActivity.class);
                startActivity(menuIntent);
                finish();
                break;
            case R.id.bNewTry:
                Intent newGameScreen = new Intent(this, GameActivity.class);
                startActivity(newGameScreen);
                gameOverDialog.dismiss();
                finish();
        }
    }

    public void dialogState() {
        if (dialogIsActive) {
            gameOverDialog.hide();
            dialogIsActive = false;
            theGameView.resumeThread();
        } else if (!dialogIsActive) {
            theGameView.pauseThread();
            gameOverDialog.show();
            dialogIsActive = true;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        dialogState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theGameView.resumeThread();
    }
}
