package com.example.cetty.derailer;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cetty.derailer.objects.SettingCard;
import com.example.cetty.derailer.objects.PlayerSelection;
import com.example.cetty.derailer.objects.Theme;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cetty on 26.07.16.
 */
public class StartMenuActivity extends Activity{
    ImageView iv_toggle, iv_back;

    static TextView tv_play;
    int connections = 4;

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    RecyclerView rv_options;
    ArrayList<SettingCard> settingCards;
    AdapterCart adapterCarts;
    static ArrayList<PlayerSelection> availablePlayers;
    RecyclerView rv_carts;



    ImageView iv_player, iv_ai_easy, iv_ai_normal, iv_ai_hard;

    static TextView tv_player_num, tv_ai_easy_num, tv_ai_normal_num, tv_ai_hard_num;
    static ImageView iv_player_icon, iv_ai_easy_icon, iv_ai_normal_icon, iv_ai_hard_icon;

    Context mContext;
    static Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // Test
        availablePlayers = new ArrayList<PlayerSelection>();
        mContext = this.getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu_v2);

        Utilities.FullScreencall(this);

        res = getResources();

        iv_player = (ImageView) findViewById(R.id.iv_player);
        iv_ai_easy = (ImageView) findViewById(R.id.iv_ai_easy);
        iv_ai_normal = (ImageView) findViewById(R.id.iv_ai_normal);
        iv_ai_hard = (ImageView) findViewById(R.id.iv_ai_hard);

        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        TextView tv_options = (TextView) findViewById(R.id.tv_options);
        TextView tv_conn_title = (TextView) findViewById(R.id.tv_connections);
        TextView tv_conn_four = (TextView) findViewById(R.id.tv_conn_four);
        TextView tv_conn_eight = (TextView) findViewById(R.id.tv_conn_eight);

        tv_header.setTypeface(MainActivity.customtf);
        tv_options.setTypeface(MainActivity.customtf);
        tv_conn_title.setTypeface(MainActivity.customtf);
        tv_conn_four.setTypeface(MainActivity.customtf);
        tv_conn_eight.setTypeface(MainActivity.customtf);


        tv_play = (TextView) findViewById(R.id.tv_play);
        tv_player_num = (TextView) findViewById(R.id.tv_player_num);
        tv_ai_easy_num = (TextView) findViewById(R.id.tv_ai_easy_num);
        tv_ai_normal_num = (TextView) findViewById(R.id.tv_ai_normal_num);
        tv_ai_hard_num = (TextView) findViewById(R.id.tv_ai_hard_num);
        tv_play.setTypeface(MainActivity.customtf);
        tv_player_num.setTypeface(MainActivity.customtf);
        tv_ai_easy_num.setTypeface(MainActivity.customtf);
        tv_ai_normal_num.setTypeface(MainActivity.customtf);
        tv_ai_hard_num.setTypeface(MainActivity.customtf);

        iv_player_icon = (ImageView) findViewById(R.id.iv_player_icon);
        iv_ai_easy_icon = (ImageView) findViewById(R.id.iv_ai_easy_icon);
        iv_ai_normal_icon = (ImageView) findViewById(R.id.iv_ai_normal_icon);
        iv_ai_hard_icon = (ImageView) findViewById(R.id.iv_ai_hard_icon);

        rv_options = (RecyclerView) findViewById(R.id.rv_options);


        iv_toggle = (ImageView) findViewById(R.id.iv_toggle);
        iv_toggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            toggle();
            }
        });

        SP = PreferenceManager.getDefaultSharedPreferences(this);
        SPE = SP.edit();

        int selectedThemeId = SP.getInt("theme",0);
        Theme gameTheme = new Theme(selectedThemeId, true);

        connections = SP.getInt("connections",4);
        if (connections == 4){
            iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state0));
        } else if (connections == 8){
            iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state1));
        }

        TypedArray cart_color_selection = res.obtainTypedArray(R.array.cart_color_selection);
        int color_default = res.getColor(R.color.cartdefault);
        for (int i = 0; i<cart_color_selection.length(); i++){
            availablePlayers.add(new PlayerSelection(cart_color_selection.getColor(i, color_default)));
        }
        rv_carts = (RecyclerView) findViewById(R.id.rv_carts);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_carts.setLayoutManager(llm);
        adapterCarts = new AdapterCart(this, gameTheme, availablePlayers);
        rv_carts.setAdapter(adapterCarts);

        // populate Options
        settingCards = new ArrayList<>();
        // has to correspond to the order of the strings in the array
        String[] option_titles = res.getStringArray(R.array.option_titles);
        ArrayList<Integer> resid_images;

        //obstacle
        SettingCard setting_obstacle = new SettingCard(mContext);
        setting_obstacle.init(option_titles[0], new ArrayList<>(Arrays.asList(R.drawable.option_obstacle_01,R.drawable.option_obstacle_02)), 0);
        settingCards.add(setting_obstacle);

        //day night
        SettingCard setting_daynight = new SettingCard(mContext);
        setting_daynight.init(option_titles[1], new ArrayList<>(Arrays.asList(R.drawable.option_obstacle_01,R.drawable.option_obstacle_02,R.drawable.option_draw_01)), 0);
        settingCards.add(setting_daynight);

        //draw
        SettingCard setting_draw = new SettingCard(mContext);
        setting_draw.init(option_titles[2], new ArrayList<>(Arrays.asList(R.drawable.option_draw_01,R.drawable.option_draw_02)), 0);
        settingCards.add(setting_draw);

        //order
        SettingCard setting_order = new SettingCard(mContext);
        setting_order.init(option_titles[3], new ArrayList<>(Arrays.asList(R.drawable.option_order_01,R.drawable.option_order_02)), 0);
        settingCards.add(setting_order);

        //sudden death
        SettingCard setting_suddendeath = new SettingCard(mContext);
        setting_suddendeath.init(option_titles[4], new ArrayList<>(Arrays.asList(R.drawable.option_suddendeath_01,R.drawable.option_suddendeath_02)), 0);
        settingCards.add(setting_suddendeath);


        AdapterOptions adapterOptions = new AdapterOptions(mContext, settingCards);
        LinearLayoutManager llm_options = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_options.setLayoutManager(llm_options);
        rv_options.setAdapter(adapterOptions);

        scaleIndicatorIcons();
        displayPlayerNumber();

        iv_player.setOnTouchListener(new MyTouchListener());
        iv_ai_easy.setOnTouchListener(new MyTouchListener());
        iv_ai_normal.setOnTouchListener(new MyTouchListener());
        iv_ai_hard.setOnTouchListener(new MyTouchListener());


        tv_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNumPlayers()>=2){
                    Intent newGameScreen= new Intent(mContext, GameActivity.class);
                    newGameScreen.putExtra("Players", availablePlayers);
                    newGameScreen.putExtra("connections", connections);
                    startActivity(newGameScreen);
                    finish();
                }
            }
        });

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void scaleIndicatorIcons(){
        // scale images
        Matrix m = new Matrix();
        m.postScale(0.3f,0.3f);

        Bitmap player_icon = Utilities.drawableToBitmap(getResources().getDrawable(R.drawable.player_icon));
        int p_width = player_icon.getWidth();
        int p_height = player_icon.getHeight();
        iv_player_icon.setImageBitmap(Bitmap.createBitmap(player_icon , 0, 0, p_width, p_height, m, true));

        m = new Matrix();
        m.postScale(0.6f,0.6f);

        Bitmap ai_easy_icon = Utilities.drawableToBitmap(getResources().getDrawable(R.drawable.ai_easy_icon));
        int ae_width = ai_easy_icon.getWidth();
        int ae_height = ai_easy_icon.getHeight();
        iv_ai_easy_icon.setImageBitmap(Bitmap.createBitmap(ai_easy_icon , 0, 0, ae_width, ae_height, m, true));

        Bitmap ai_normal_icon = Utilities.drawableToBitmap(getResources().getDrawable(R.drawable.ai_normal_icon));
        int an_width = ai_normal_icon.getWidth();
        int an_height = ai_normal_icon.getHeight();
        iv_ai_normal_icon.setImageBitmap(Bitmap.createBitmap(ai_normal_icon , 0, 0, an_width, an_height, m, true));

        Bitmap ai_hard_icon = Utilities.drawableToBitmap(getResources().getDrawable(R.drawable.ai_hard_icon));
        int ah_width = ai_hard_icon.getWidth();
        int ah_height = ai_hard_icon.getHeight();
        iv_ai_hard_icon.setImageBitmap(Bitmap.createBitmap(ai_hard_icon , 0, 0, ah_width, ah_height, m, true));
    }

    static public void displayPlayerNumber(){
        if (getNumPlayers()<2){
            tv_play.setBackground(res.getDrawable(R.drawable.button01));
        } else {
            tv_play.setBackground(res.getDrawable(R.drawable.button02));
        }
        int numPlayers = 0;
        int numAiEasy = 0;
        int numAiNormal = 0;
        int numAiHard = 0;
        for (PlayerSelection player : availablePlayers){
            if (player.isPlayer()){
                numPlayers += 1;
            } else if (player.isEasyAI()){
                numAiEasy += 1;
            } else if (player.isNormalAI()){
                numAiNormal += 1;
            } else if (player.isHardAI()){
                numAiHard += 1;
            }
        }
        if (numPlayers == 0){
            iv_player_icon.setVisibility(View.GONE);
            tv_player_num.setVisibility(View.GONE);
        } else {
            iv_player_icon.setVisibility(View.VISIBLE);
            tv_player_num.setVisibility(View.VISIBLE);
        }
        if (numAiEasy == 0){
            iv_ai_easy_icon.setVisibility(View.GONE);
            tv_ai_easy_num.setVisibility(View.GONE);
        } else {
            iv_ai_easy_icon.setVisibility(View.VISIBLE);
            tv_ai_easy_num.setVisibility(View.VISIBLE);
        }
        if (numAiNormal == 0){
            iv_ai_normal_icon.setVisibility(View.GONE);
            tv_ai_normal_num.setVisibility(View.GONE);
        } else {
            iv_ai_normal_icon.setVisibility(View.VISIBLE);
            tv_ai_normal_num.setVisibility(View.VISIBLE);
        }
        if (numAiHard == 0){
            iv_ai_hard_icon.setVisibility(View.GONE);
            tv_ai_hard_num.setVisibility(View.GONE);
        } else {
            iv_ai_hard_icon.setVisibility(View.VISIBLE);
            tv_ai_hard_num.setVisibility(View.VISIBLE);
        }

        tv_player_num.setText(String.valueOf(numPlayers));
        tv_ai_easy_num.setText(String.valueOf(numAiEasy));
        tv_ai_normal_num.setText(String.valueOf(numAiNormal));
        tv_ai_hard_num.setText(String.valueOf(numAiHard));
    }

    void toggle(){
        if (connections == 4){
            iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state1));
            connections=8;
        } else if (connections == 8){
            iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state0));
            connections=4;
        }
        SPE.putInt("connections",connections);
        SPE.commit();
    }


    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    static int getNumPlayers(){
        int count = 0;
        for (PlayerSelection player : availablePlayers){
            if (!player.isUnselected()){
                count += 1;
            }
        }
        return count;
    }


}
