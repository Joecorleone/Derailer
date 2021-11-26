package de.badgersburrow.derailer;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.adapters.AdapterCart;
import de.badgersburrow.derailer.adapters.AdapterOptions;
import de.badgersburrow.derailer.adapters.AdapterPlayers;
import de.badgersburrow.derailer.views.GameTextButton;
import de.badgersburrow.derailer.views.SettingCard;
import de.badgersburrow.derailer.objects.PlayerSelection;
import de.badgersburrow.derailer.objects.Theme;
import de.badgersburrow.derailer.views.ButtonRecyclerView;
import de.badgersburrow.derailer.views.GameSignButton;

import java.util.ArrayList;

/**
 * Created by cetty on 26.07.16.
 */
public class FragmentGameSettings extends Fragment implements AdapterCart.ChangeListener, AdapterOptions.SoundListener {

    private static String TAG = "StartMenuActivity";

    int connections = 4;

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    RecyclerView rv_options;
    AdapterOptions adapterOptions;
    ArrayList<SettingCard> settingCards;

    AdapterCart adapterCarts;
    static ArrayList<PlayerSelection> availablePlayers;
    ButtonRecyclerView rv_carts;

    RecyclerView rv_players;
    AdapterPlayers adapterPlayers;


    ImageView iv_player, iv_ai_easy, iv_ai_normal, iv_ai_hard;

    Button bt_back;
    GameTextButton bt_play;
    ToggleButton tb_toggle;

    static Resources res;

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_game_settings, container, false);

        availablePlayers = new ArrayList<>();

        super.onCreate(savedInstanceState);

        res = getResources();

        iv_player = rootView.findViewById(R.id.iv_player);
        iv_ai_easy = rootView.findViewById(R.id.iv_ai_easy);
        iv_ai_normal = rootView.findViewById(R.id.iv_ai_normal);
        iv_ai_hard = rootView.findViewById(R.id.iv_ai_hard);

        TextView tv_header = rootView.findViewById(R.id.tv_header);
        TextView tv_players = rootView.findViewById(R.id.tv_players);
        TextView tv_human = rootView.findViewById(R.id.tv_human);
        TextView tv_ai = rootView.findViewById(R.id.tv_ai);
        TextView tv_options = rootView.findViewById(R.id.tv_options);
        TextView tv_conn_title = rootView.findViewById(R.id.tv_connections);

        tv_header.setTypeface(getAct().customtf_normal);
        tv_players.setTypeface(getAct().customtf_normal);
        tv_human.setTypeface(getAct().customtf_normal);
        tv_ai.setTypeface(getAct().customtf_normal);
        tv_options.setTypeface(getAct().customtf_normal);
        tv_conn_title.setTypeface(getAct().customtf_normal);

        bt_play = rootView.findViewById(R.id.bt_play);
        bt_play.setTypeface(getAct().customtf_normal);

        rv_options = rootView.findViewById(R.id.rv_options);
        rv_players = rootView.findViewById(R.id.rv_players);


        tb_toggle = rootView.findViewById(R.id.tb_toggle);
        tb_toggle.setOnClickListener(v -> toggle(v));

        SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        SPE = SP.edit();

        int selectedThemeId = SP.getInt("theme",0);
        Theme gameTheme = new Theme(selectedThemeId, true);

        connections = SP.getInt("connections",4);

        tb_toggle.setChecked(connections == 8);
        toggleAnimate(tb_toggle);

        TypedArray cart_color_selection = res.obtainTypedArray(R.array.cart_color_selection);
        int color_default = res.getColor(R.color.cartdefault);
        for (int i = 0; i<cart_color_selection.length(); i++){
            availablePlayers.add(new PlayerSelection(cart_color_selection.getColor(i, color_default)));
        }
        cart_color_selection.recycle();
        rv_carts = rootView.findViewById(R.id.rv_carts);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_carts.setLayoutManager(llm);
        adapterCarts = new AdapterCart(getContext(), gameTheme, availablePlayers, this);
        rv_carts.setAdapter(adapterCarts);

        rv_carts.setBt_left(rootView.findViewById(R.id.bt_carts_left));
        rv_carts.setBt_right(rootView.findViewById(R.id.bt_carts_right));



        // populate Options
        settingCards = new ArrayList<>();
        // has to correspond to the order of the strings in the array

        //obstacle
        SettingCard setting_obstacle = new SettingCard(getContext(), Keys.option_obstacle);
        setting_obstacle.addChoice(new SettingCard.Choice(Keys.option_obstacle_none, getString(R.string.option_obstacle_none), R.drawable.option_obstacle_none));
        setting_obstacle.addChoice(new SettingCard.Choice(Keys.option_obstacle_few, getString(R.string.option_obstacle_few), R.drawable.option_obstacle_few));
        setting_obstacle.addChoice(new SettingCard.Choice(Keys.option_obstacle_many, getString(R.string.option_obstacle_many), R.drawable.option_obstacle_many));
        setting_obstacle.init(getString(R.string.option_obstacle), Keys.option_obstacle_none);
        settingCards.add(setting_obstacle);

        //day night
        /*SettingCard setting_daynight = new SettingCard(mContext);
        setting_daynight.addChoice(new SettingCard.Choice("key_daynight_01", getString(R.string.option_daynight_01), R.drawable.option_daynight_01));
        setting_daynight.addChoice(new SettingCard.Choice("key_daynight_02", getString(R.string.option_daynight_02), R.drawable.option_daynight_02));
        setting_daynight.init(getString(R.string.option_daynight), SP.getString(Keys.option_daynight, Keys.option_daynight_01));
        settingCards.add(setting_daynight);*/

        //draw
        SettingCard setting_draw = new SettingCard(getContext(), Keys.option_draw);
        setting_draw.addChoice(new SettingCard.Choice(Keys.option_draw_fill, getString(R.string.option_draw_01), R.drawable.option_draw_01));
        setting_draw.addChoice(new SettingCard.Choice(Keys.option_draw_new, getString(R.string.option_draw_02), R.drawable.option_draw_02));
        setting_draw.init(getString(R.string.option_draw), Keys.option_draw_new);
        settingCards.add(setting_draw);

        //order
        SettingCard setting_order = new SettingCard(getContext(), Keys.option_order);
        setting_order.addChoice(new SettingCard.Choice(Keys.option_order_same, getString(R.string.option_order_01), R.drawable.option_order_01));
        setting_order.addChoice(new SettingCard.Choice(Keys.option_order_random, getString(R.string.option_order_02), R.drawable.option_order_02));
        setting_order.init(getString(R.string.option_order), Keys.option_order_same);
        settingCards.add(setting_order);

        //sudden death
        /*SettingCard setting_suddendeath = new SettingCard(mContext);
        setting_suddendeath.addChoice(new SettingCard.Choice(Keys.option_suddendeath_01, getString(R.string.option_suddendeath_01), R.drawable.option_suddendeath_01));
        setting_suddendeath.addChoice(new SettingCard.Choice(Keys.option_suddendeath_02, getString(R.string.option_suddendeath_02), R.drawable.option_suddendeath_02));
        setting_suddendeath.init(getString(R.string.option_suddendeath), SP.getString(Keys.option_suddendeath, Keys.option_suddendeath_01));
        settingCards.add(setting_suddendeath);*/

        //order
        SettingCard setting_victory = new SettingCard(getContext(), Keys.option_victory);
        setting_victory.addChoice(new SettingCard.Choice(Keys.option_victory_last, getString(R.string.option_victory_last), R.drawable.option_victory_01));
        setting_victory.addChoice(new SettingCard.Choice(Keys.option_victory_distance, getString(R.string.option_victory_dist), R.drawable.option_victory_02));
        setting_victory.init(getString(R.string.option_victory), Keys.option_victory_last);
        settingCards.add(setting_victory);

        //collision
        SettingCard setting_collision = new SettingCard(getContext(), Keys.option_collision);
        setting_collision.addChoice(new SettingCard.Choice(Keys.option_collision_on, getString(R.string.option_collision_on), R.drawable.option_collision_02));
        setting_collision.addChoice(new SettingCard.Choice(Keys.option_collision_off, getString(R.string.option_collision_off), R.drawable.option_collision_01));
        setting_collision.init(getString(R.string.option_collision), Keys.option_collision_on);
        settingCards.add(setting_collision);


        adapterOptions = new AdapterOptions(settingCards, this);
        LinearLayoutManager llm_options = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_options.setLayoutManager(llm_options);
        rv_options.setAdapter(adapterOptions);
        if (connections == 8){
            adapterOptions.enable(Keys.option_draw);
        } else {
            adapterOptions.disable(Keys.option_draw);
        }


        iv_player.setOnTouchListener(new MyTouchListener());
        iv_ai_easy.setOnTouchListener(new MyTouchListener());
        iv_ai_normal.setOnTouchListener(new MyTouchListener());
        iv_ai_hard.setOnTouchListener(new MyTouchListener());

        //Utilities.selector(bt_play, R.drawable.button_play_state02, R.drawable.button_play_state01, R.drawable.button_play_state03);
        bt_play.setDrawableDisabled(R.drawable.button_play_state01);
        bt_play.setDrawablePressed(R.drawable.button_play_state03);
        bt_play.setDrawableNormal(R.drawable.button_play_state02);
        bt_play.setOnClickListener(v -> {
            if (getNumPlayers() < 2) {
                showDialog();
            } else {
                Bundle b = new Bundle();
                b.putSerializable("Players", adapterCarts.getSelected());
                b.putStringArrayList("Options", adapterOptions.getKeys());
                b.putInt("connections", connections);
                getAct().showGame(b);
            }
        });

        GameSignButton gsb_back = rootView.findViewById(R.id.gsb_back);
        gsb_back.setOnClickListener(view -> {
            getAct().playSoundSign();
            getAct().onBackPressed();
        });


        GameSignButton gsb_play = (GameSignButton) rootView.findViewById(R.id.gsb_play);
        gsb_play.setOnClickListener(view -> {
            getAct().playSoundSign();
            if (getNumPlayers() < 2) {
                showDialog();
            } else {
                Bundle b = new Bundle();
                b.putSerializable("Players", adapterCarts.getSelected());
                b.putStringArrayList("Options", adapterOptions.getKeys());
                b.putInt("connections", connections);
                getAct().showGame(b);
            }
        });


        bt_back = rootView.findViewById(R.id.bt_back);
        bt_back.setOnClickListener(view -> getAct().onBackPressed());

        adapterPlayers = new AdapterPlayers(getContext());
        GridLayoutManager glm_players = new GridLayoutManager(getContext(), 2);
        rv_players.setLayoutManager(glm_players);
        rv_players.setAdapter(adapterPlayers);

        return rootView;
    }

    void toggle(View v){
        if (connections == 4){
            //iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state1));
            connections=8;
            adapterOptions.enable(Keys.option_draw);
        } else if (connections == 8){
            //iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.toggle_state0));
            connections=4;
            adapterOptions.disable(Keys.option_draw);
        }
        toggleAnimate(v);
        Log.d(TAG, "connections: " + connections);
        SPE.putInt("connections",connections);
        SPE.commit();
    }

    void toggleAnimate(View v){
        getAct().playSoundToggle();
        StateListDrawable stateListDrawable = (StateListDrawable) v.getBackground();
        AnimationDrawable animationDrawable = (AnimationDrawable) stateListDrawable.getCurrent();
        animationDrawable.start();
    }

    @Override
    public void playerSelected(String key) {
        adapterPlayers.addPlayer(key);
    }

    @Override
    public void playerDeselected(String key) {
        adapterPlayers.removePlayer(key);
    }

    @Override
    public void playSoundOptionChanged() {
        getAct().playSoundOption();
    }


    private static final class MyTouchListener implements View.OnTouchListener {
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
        final Dialog dialog = new Dialog(getContext(), R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.dialog_few_player);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_title = dialog.findViewById(R.id.tv_title);
        tv_title.setTypeface(getAct().customtf_normal);
        TextView tv_description = dialog.findViewById(R.id.tv_description);
        tv_description.setTypeface(getAct().customtf_normal);
        ImageView iv_ok = dialog.findViewById(R.id.iv_ok);

        iv_ok.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }

}
