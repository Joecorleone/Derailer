package de.badgersburrow.derailer;

/**
 * Created by cetty on 15.01.17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import de.badgersburrow.derailer.R;

import java.util.ArrayList;

public class GameOverActivity extends Activity implements OnClickListener{

    private Button bReplay;
    private Button bExit;
    private TextView player;
    ArrayList<String> players;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        Bundle extras = getIntent().getExtras();
        String playerName = extras.getString("player");
        players = extras.getStringArrayList("Players");
        player = (TextView) findViewById(R.id.textPlayer);
        player.setText(playerName);
        bReplay = (Button) findViewById(R.id.bNewTry);
        bExit = (Button) findViewById(R.id.bMainMenu);
        bReplay.setOnClickListener(this);
        bExit.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bNewTry:
                Intent newGameScreen= new Intent(getApplicationContext(), GameActivity.class);
                newGameScreen.putExtra("Players", players);
                startActivity(newGameScreen);
                this.finish();
                break;
            case R.id.bMainMenu:
                this.finish();
                break;
        }
    }

}