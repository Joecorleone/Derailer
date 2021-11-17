package de.badgersburrow.derailer;

/**
 * Created by cetty on 15.01.17.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.badgersburrow.derailer.objects.PlayerResult;
import de.badgersburrow.derailer.objects.PlayerSelection;

public class GameOverActivity extends Activity implements OnClickListener{

    private Button bReplay;
    private Button bExit;
    private TextView tv_place1_dist, tv_place2_dist, tv_place3_dist, tv_place1_label, tv_place2_label, tv_place3_label;
    private ImageView iv_place1_main, iv_place2_main, iv_place3_main, iv_place1_color, iv_place2_color, iv_place3_color;
    ArrayList<PlayerResult> players;
    ArrayList<PlayerSelection> playersSelection;
    ArrayList<String> options;

    SharedPreferences SP;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Bundle extras = getIntent().getExtras();
        //String playerName = extras.getString("playerLabel");
        //int playercolor = extras.getInt("playerColor");
        playersSelection = (ArrayList<PlayerSelection>)extras.getSerializable("PlayersSelection");
        players = (ArrayList<PlayerResult>)extras.getSerializable("Players");
        options = extras.getStringArrayList("Options");

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        int selectedThemeId = SP.getInt("theme",0);

        TextView tv_header = findViewById(R.id.tv_header);
        tv_header.setTypeface(MainActivity.customtf_normal);

        // podium
        tv_place1_dist = (TextView) findViewById(R.id.tv_place1_dist);
        tv_place2_dist = (TextView) findViewById(R.id.tv_place2_dist);
        tv_place3_dist = (TextView) findViewById(R.id.tv_place3_dist);
        tv_place1_label = (TextView) findViewById(R.id.tv_place1_label);
        tv_place2_label = (TextView) findViewById(R.id.tv_place2_label);
        tv_place3_label = (TextView) findViewById(R.id.tv_place3_label);
        iv_place1_main = (ImageView) findViewById(R.id.iv_place1_main);
        iv_place2_main = (ImageView) findViewById(R.id.iv_place2_main);
        iv_place3_main = (ImageView) findViewById(R.id.iv_place3_main);
        iv_place1_color = (ImageView) findViewById(R.id.iv_place1_color);
        iv_place2_color = (ImageView) findViewById(R.id.iv_place2_color);
        iv_place3_color = (ImageView) findViewById(R.id.iv_place3_color);

        List<TextView> list_tv_dist = Arrays.asList(tv_place1_dist, tv_place2_dist, tv_place3_dist);
        List<TextView> list_tv_label = Arrays.asList(tv_place1_label, tv_place2_label, tv_place3_label);
        List<ImageView> list_iv_main = Arrays.asList(iv_place1_main, iv_place2_main, iv_place3_main);
        List<ImageView> list_iv_color = Arrays.asList(iv_place1_color, iv_place2_color, iv_place3_color);

        if (options.contains(Keys.option_victory_01)){
            tv_place1_dist.setVisibility(View.INVISIBLE);
            tv_place2_dist.setVisibility(View.INVISIBLE);
            tv_place3_dist.setVisibility(View.INVISIBLE);

            Collections.sort(players,
                    (o1, o2) -> -Integer.compare(o1.getOutCount(), o2.getOutCount()));
        } else {
            Collections.sort(players,
                    (o1, o2) -> -Integer.compare(o1.getTileCount(), o2.getTileCount()));
        }

        Matrix m = new Matrix();
        m.postRotate(270);
        //float scale = getResources().getFraction(R.fraction.cart_scale, 1, 1);
        //m.postScale(scale, scale);

        for (int i=0; i<3 && i<players.size(); i++){
            PlayerResult p = players.get(i);
            list_tv_dist.get(i).setTypeface(MainActivity.customtf_normal);
            list_tv_dist.get(i).setText(String.valueOf(p.getTileCount()));
            list_tv_label.get(i).setTypeface(MainActivity.customtf_normal);
            list_tv_label.get(i).setText(p.getLabel(this));
            list_tv_label.get(i).setTextColor(p.getColor());

            Bitmap bmpColor = p.getBmpColor(this, selectedThemeId);
            int width = bmpColor.getWidth();
            int height = bmpColor.getHeight();
            list_iv_color.get(i).setImageBitmap(Bitmap.createBitmap(bmpColor , 0, 0, width, height, m, true));
            list_iv_color.get(i).setColorFilter(p.getColor());
            list_iv_main.get(i).setImageBitmap(Bitmap.createBitmap(p.getBmpMain(this, selectedThemeId) , 0, 0, width, height, m, true));
        }

        if (players.size() < 3){
            LinearLayoutCompat ll_place3_podium = findViewById(R.id.ll_place3_podium);
            ll_place3_podium.setVisibility(View.INVISIBLE);
        }

        bReplay = (Button) findViewById(R.id.bNewTry);
        bExit = (Button) findViewById(R.id.bMainMenu);
        bReplay.setOnClickListener(this);
        bExit.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bNewTry:
                Intent newGameScreen= new Intent(getApplicationContext(), GameActivity.class);
                newGameScreen.putExtra("Players", playersSelection);
                newGameScreen.putExtra("Options", options);
                startActivity(newGameScreen);
                this.finish();
                break;
            case R.id.bMainMenu:
                this.finish();
                break;
        }
    }

}