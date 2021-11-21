package de.badgersburrow.derailer;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.objects.GameTextButton;
import de.badgersburrow.derailer.objects.SettingCard;
import de.badgersburrow.derailer.objects.PlayerSelection;
import de.badgersburrow.derailer.objects.Theme;
import de.badgersburrow.derailer.views.ButtonRecyclerView;

import java.util.ArrayList;

/**
 * Created by cetty on 26.07.16.
 */
public class StartMenuActivity extends Activity{

    private static String TAG = "StartMenuActivity";

    int connections = 4;

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    RecyclerView rv_options;
    ArrayList<SettingCard> settingCards;
    AdapterCart adapterCarts;
    static ArrayList<PlayerSelection> availablePlayers;
    ButtonRecyclerView rv_carts;



    ImageView iv_player, iv_ai_easy, iv_ai_normal, iv_ai_hard;

    Button bt_back;
    static GameTextButton bt_play;
    static TextView tv_player_num, tv_ai_easy_num, tv_ai_normal_num, tv_ai_hard_num;
    static ImageView iv_player_icon, iv_ai_easy_icon, iv_ai_normal_icon, iv_ai_hard_icon;
    static ToggleButton tb_toggle;

    Context mContext;
    static Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // Test
        availablePlayers = new ArrayList<>();
        mContext = this.getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        Utilities.FullScreencall(this);

        /*decorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                    }
                }
        );*/

        res = getResources();

        iv_player = (ImageView) findViewById(R.id.iv_player);
        iv_ai_easy = (ImageView) findViewById(R.id.iv_ai_easy);
        iv_ai_normal = (ImageView) findViewById(R.id.iv_ai_normal);
        iv_ai_hard = (ImageView) findViewById(R.id.iv_ai_hard);

        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        TextView tv_players = (TextView) findViewById(R.id.tv_players);
        TextView tv_human = (TextView) findViewById(R.id.tv_human);
        TextView tv_ai = (TextView) findViewById(R.id.tv_ai);
        TextView tv_options = (TextView) findViewById(R.id.tv_options);
        TextView tv_conn_title = (TextView) findViewById(R.id.tv_connections);
        //TextView tv_conn_four = (TextView) findViewById(R.id.tv_conn_four);
        //TextView tv_conn_eight = (TextView) findViewById(R.id.tv_conn_eight);

        tv_header.setTypeface(MainActivity.customtf_normal);
        tv_players.setTypeface(MainActivity.customtf_normal);
        tv_human.setTypeface(MainActivity.customtf_normal);
        tv_ai.setTypeface(MainActivity.customtf_normal);
        tv_options.setTypeface(MainActivity.customtf_normal);
        tv_conn_title.setTypeface(MainActivity.customtf_normal);
        //tv_conn_four.setTypeface(MainActivity.customtf_normal);
        //tv_conn_eight.setTypeface(MainActivity.customtf_normal);

        bt_play = (GameTextButton) findViewById(R.id.bt_play);
        tv_player_num = (TextView) findViewById(R.id.tv_player_num);
        tv_ai_easy_num = (TextView) findViewById(R.id.tv_ai_easy_num);
        tv_ai_normal_num = (TextView) findViewById(R.id.tv_ai_normal_num);
        tv_ai_hard_num = (TextView) findViewById(R.id.tv_ai_hard_num);
        bt_play.setTypeface(MainActivity.customtf_normal);
        tv_player_num.setTypeface(MainActivity.customtf_normal);
        tv_ai_easy_num.setTypeface(MainActivity.customtf_normal);
        tv_ai_normal_num.setTypeface(MainActivity.customtf_normal);
        tv_ai_hard_num.setTypeface(MainActivity.customtf_normal);

        iv_player_icon = (ImageView) findViewById(R.id.iv_player_icon);
        iv_ai_easy_icon = (ImageView) findViewById(R.id.iv_ai_easy_icon);
        iv_ai_normal_icon = (ImageView) findViewById(R.id.iv_ai_normal_icon);
        iv_ai_hard_icon = (ImageView) findViewById(R.id.iv_ai_hard_icon);

        rv_options = (RecyclerView) findViewById(R.id.rv_options);


        tb_toggle = (ToggleButton) findViewById(R.id.tb_toggle);
        tb_toggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(v);
            }
        });

        //animationDrawable.selectDrawable(animationDrawable.getNumberOfFrames() - 1);


        SP = PreferenceManager.getDefaultSharedPreferences(this);
        SPE = SP.edit();

        int selectedThemeId = SP.getInt("theme",0);
        Theme gameTheme = new Theme(selectedThemeId, true);

        connections = SP.getInt("connections",4);
        tb_toggle.setChecked(connections == 8);
        StateListDrawable stateListDrawable = (StateListDrawable) tb_toggle.getBackground();
        AnimationDrawable animationDrawable = (AnimationDrawable) stateListDrawable.getCurrent();
        animationDrawable.start();

        TypedArray cart_color_selection = res.obtainTypedArray(R.array.cart_color_selection);
        int color_default = res.getColor(R.color.cartdefault);
        for (int i = 0; i<cart_color_selection.length(); i++){
            availablePlayers.add(new PlayerSelection(cart_color_selection.getColor(i, color_default)));
        }
        rv_carts = findViewById(R.id.rv_carts);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_carts.setLayoutManager(llm);
        adapterCarts = new AdapterCart(this, gameTheme, availablePlayers);
        rv_carts.setAdapter(adapterCarts);

        rv_carts.setBt_left(findViewById(R.id.bt_carts_left));
        rv_carts.setBt_right(findViewById(R.id.bt_carts_right));



        // populate Options
        settingCards = new ArrayList<>();
        // has to correspond to the order of the strings in the array

        //obstacle
        SettingCard setting_obstacle = new SettingCard(mContext);
        setting_obstacle.addChoice(new SettingCard.Choice(Keys.option_obstacle_01, getString(R.string.option_obstacle_01), R.drawable.option_obstacle_01));
        setting_obstacle.addChoice(new SettingCard.Choice(Keys.option_obstacle_02, getString(R.string.option_obstacle_02), R.drawable.option_obstacle_02));
        setting_obstacle.init(getString(R.string.option_obstacle), SP.getString(Keys.option_obstacle, Keys.option_obstacle_01));
        settingCards.add(setting_obstacle);

        //day night
        /*SettingCard setting_daynight = new SettingCard(mContext);
        setting_daynight.addChoice(new SettingCard.Choice("key_daynight_01", getString(R.string.option_daynight_01), R.drawable.option_daynight_01));
        setting_daynight.addChoice(new SettingCard.Choice("key_daynight_02", getString(R.string.option_daynight_02), R.drawable.option_daynight_02));
        setting_daynight.init(getString(R.string.option_daynight), SP.getString(Keys.option_daynight, Keys.option_daynight_01));
        settingCards.add(setting_daynight);*/

        //draw
        SettingCard setting_draw = new SettingCard(mContext);
        setting_draw.addChoice(new SettingCard.Choice(Keys.option_draw_01, getString(R.string.option_draw_01), R.drawable.option_draw_01));
        setting_draw.addChoice(new SettingCard.Choice(Keys.option_draw_02, getString(R.string.option_draw_02), R.drawable.option_draw_02));
        setting_draw.init(getString(R.string.option_draw), SP.getString(Keys.option_draw, Keys.option_draw_01));
        settingCards.add(setting_draw);

        //order
        SettingCard setting_order = new SettingCard(mContext);
        setting_order.addChoice(new SettingCard.Choice(Keys.option_order_01, getString(R.string.option_order_01), R.drawable.option_order_01));
        setting_order.addChoice(new SettingCard.Choice(Keys.option_order_02, getString(R.string.option_order_02), R.drawable.option_order_02));
        setting_order.init(getString(R.string.option_order), SP.getString(Keys.option_order, Keys.option_order_01));
        settingCards.add(setting_order);

        //sudden death
        /*SettingCard setting_suddendeath = new SettingCard(mContext);
        setting_suddendeath.addChoice(new SettingCard.Choice(Keys.option_suddendeath_01, getString(R.string.option_suddendeath_01), R.drawable.option_suddendeath_01));
        setting_suddendeath.addChoice(new SettingCard.Choice(Keys.option_suddendeath_02, getString(R.string.option_suddendeath_02), R.drawable.option_suddendeath_02));
        setting_suddendeath.init(getString(R.string.option_suddendeath), SP.getString(Keys.option_suddendeath, Keys.option_suddendeath_01));
        settingCards.add(setting_suddendeath);*/

        //order
        SettingCard setting_victory = new SettingCard(mContext);
        setting_victory.addChoice(new SettingCard.Choice(Keys.option_victory_01, getString(R.string.option_victory_01), R.drawable.option_victory_01));
        setting_victory.addChoice(new SettingCard.Choice(Keys.option_victory_02, getString(R.string.option_victory_02), R.drawable.option_victory_02));
        setting_victory.init(getString(R.string.option_victory),SP.getString(Keys.option_victory, Keys.option_victory_01));
        settingCards.add(setting_victory);


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

        //Utilities.selector(bt_play, R.drawable.button_play_state02, R.drawable.button_play_state01, R.drawable.button_play_state03);
        bt_play.setDrawableDisabled(R.drawable.button_play_state01);
        bt_play.setDrawablePressed(R.drawable.button_play_state03);
        bt_play.setDrawableNormal(R.drawable.button_play_state02);
        bt_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNumPlayers() < 2) {
                    /*AlertDialog.Builder builder =
                            new AlertDialog.Builder(StartMenuActivity.this, R.style.AlertDialogCustom);
                    builder.setTitle("Too few");
                    builder.setMessage("Add at least two player!");
                    builder.setPositiveButton("OK", null);//second parameter used for onclicklistener
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();*/
                    showDialog();
                } else {
                    Intent newGameScreen= new Intent(mContext, GameActivity.class);
                    newGameScreen.putExtra("Players", adapterCarts.getSelected());
                    newGameScreen.putExtra("Options", adapterOptions.getKeys());
                    newGameScreen.putExtra("connections", connections);
                    startActivity(newGameScreen);
                    finish();
                }
            }
        });

        bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
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
        /*if (getNumPlayers()<2){
            bt_play.setEnabled(false);
            //tv_play.setBackground(res.getDrawable(R.drawable.button_play01));
        } else {
            bt_play.setEnabled(true);
            //tv_play.setBackground(res.getDrawable(R.drawable.button_play02));
        }*/
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

    void toggle(View v){
        if (connections == 4){
            //iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state1));
            connections=8;
        } else if (connections == 8){
            //iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state0));
            connections=4;
        }
        StateListDrawable stateListDrawable = (StateListDrawable) v.getBackground();
        AnimationDrawable animationDrawable = (AnimationDrawable) stateListDrawable.getCurrent();
        animationDrawable.start();
        Log.d(TAG, "connections: " + connections);
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


    public void showDialog() {

        final Dialog dialog = new Dialog(this, R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_few_player);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //new ColorDrawable(Color.TRANSPARENT)

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setTypeface(MainActivity.customtf_normal);
        TextView tv_description = dialog.findViewById(R.id.tv_description);
        tv_description.setTypeface(MainActivity.customtf_normal);
        ImageView iv_ok = dialog.findViewById(R.id.iv_ok);

        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }



}
