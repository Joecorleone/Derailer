package com.example.cetty.derailer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.SurfaceView;
import android.content.DialogInterface.OnDismissListener;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cetty.derailer.objects.AnimationPath;
import com.example.cetty.derailer.objects.PlayerSelection;

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
    private ImageView iv_back;

    Runnable notificationRun;


    public static AnimationPath animPath;

    Typeface customtf;

    RelativeLayout rl_notification;
    ImageView iv_n_background, iv_n_player_color, iv_n_player;
    TextView tv_n_texttop, tv_n_textbig, tv_n_textbottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.FullScreencall(this);

        playerSelection = (ArrayList<PlayerSelection>) this.getIntent().getExtras().getSerializable("Players");
        connections = this.getIntent().getIntExtra("connections",4);
        animPath = new AnimationPath(connections);

        customtf = Typeface.createFromAsset(getAssets(), "fonts/Acme-Regular.ttf");
        SP = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedThemeId = SP.getInt("theme",0);
        setContentView(R.layout.activity_game);
        RelativeLayout rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        theGameView = new GameView(this, playerSelection, connections, new GameTheme(this, selectedThemeId));
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

        showNotification(theGameView.players.get(0));

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void showNotification(final Player player){
        runOnUiThread(notificationRun = new Runnable() {
            @Override
            public void run() {

                iv_n_background.setColorFilter(player.getColor(), PorterDuff.Mode.SRC_IN);
                iv_n_player.setImageBitmap(player.getBmpMain());
                iv_n_player_color.setImageBitmap(player.getBmpColor());
                iv_n_player_color.setColorFilter(player.getColor(), PorterDuff.Mode.SRC_IN);
                tv_n_texttop.setTypeface(customtf);
                tv_n_textbig.setTypeface(customtf);
                tv_n_textbottom.setTypeface(customtf);
                rl_notification.setVisibility(View.VISIBLE);

                if ((!player.virtual && player.alive ) || (player.virtual && player.aliveVirtual )){
                    String gp = theGameView.gamePhase;
                    if (gp.equals(theGameView.gpStart)) {
                        tv_n_texttop.setVisibility(View.GONE);
                        tv_n_textbottom.setText("choose your start spot");
                    } else if (gp.equals(theGameView.gpPlaying)) {
                        tv_n_texttop.setText("On to ");
                        tv_n_textbottom.setText("place a tile");
                    }
                } else if ((!player.virtual && !player.alive ) || (player.virtual && !player.aliveVirtual )){
                    tv_n_texttop.setText("Kaboom");
                    tv_n_textbottom.setText("is out!");
                }

            }
        });

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideNotification();
                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        thread.start();
    }

    public void hideNotification(){
        rl_notification.setVisibility(View.INVISIBLE);
    }


    public void onGameOver(String player) {
        Intent theNextIntent = new Intent(getApplicationContext(),
                GameOverActivity.class);
        theNextIntent.putExtra("player", player);
        theNextIntent.putExtra("Players", playerSelection);
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
}
