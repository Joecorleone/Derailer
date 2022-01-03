package de.badgersburrow.derailer;

import android.app.Dialog;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.adapters.AdapterCart;
import de.badgersburrow.derailer.adapters.AdapterOptions;
import de.badgersburrow.derailer.adapters.AdapterPlayers;
import de.badgersburrow.derailer.databinding.DialogFewPlayerBinding;
import de.badgersburrow.derailer.databinding.FragmentGameSettingsBinding;
import de.badgersburrow.derailer.views.NoScrollGridLayoutManager;
import de.badgersburrow.derailer.views.SettingCard;
import de.badgersburrow.derailer.objects.PlayerSelection;
import de.badgersburrow.derailer.objects.Theme;

import java.util.ArrayList;

/**
 * Created by cetty on 26.07.16.
 */
public class FragmentGameSettings extends Fragment implements AdapterCart.ChangeListener, AdapterOptions.SoundListener, View.OnTouchListener {

    private static String TAG = "StartMenuActivity";

    FragmentGameSettingsBinding binding;

    SharedPreferences SP;
    SharedPreferences.Editor SPE;

    AdapterOptions adapterOptions;
    ArrayList<SettingCard> settingCards;

    AdapterCart adapterCarts;
    static ArrayList<PlayerSelection> availablePlayers;

    AdapterPlayers adapterPlayers;

    static Resources res;

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGameSettingsBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        //View rootView = inflater.inflate(R.layout.fragment_game_settings, container, false);

        availablePlayers = new ArrayList<>();

        super.onCreate(savedInstanceState);

        res = getResources();

        binding.tvHeader.setTypeface(getAct().customtf_normal);
        binding.tvPlayers.setTypeface(getAct().customtf_normal);
        binding.tvHuman.setTypeface(getAct().customtf_normal);
        binding.tvAi.setTypeface(getAct().customtf_normal);
        binding.tvOptions.setTypeface(getAct().customtf_normal);

        SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        SPE = SP.edit();

        int selectedThemeId = SP.getInt("theme",0);
        Theme gameTheme = new Theme(selectedThemeId, true);

        TypedArray cart_color_selection = res.obtainTypedArray(R.array.cart_color_selection);
        int color_default = res.getColor(R.color.cartdefault);
        for (int i = 0; i<cart_color_selection.length(); i++){
            availablePlayers.add(new PlayerSelection(cart_color_selection.getColor(i, color_default)));
        }
        cart_color_selection.recycle();
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvCarts.setLayoutManager(llm);
        adapterCarts = new AdapterCart(getContext(), gameTheme, availablePlayers, this);
        adapterCarts.setSoundListener(getAct());
        binding.rvCarts.setAdapter(adapterCarts);
        binding.rvCarts.setListener(getAct());
        binding.rvCarts.setBtLeft(binding.btCartsLeft);
        binding.rvCarts.setBtRight(binding.btCartsRight);



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
        setting_draw.addChoice(new SettingCard.Choice(Keys.option_draw_fill, getString(R.string.option_draw_01), R.drawable.option_draw_fill));
        setting_draw.addChoice(new SettingCard.Choice(Keys.option_draw_new, getString(R.string.option_draw_02), R.drawable.option_draw_new));
        setting_draw.init(getString(R.string.option_draw), Keys.option_draw_new);
        settingCards.add(setting_draw);

        //order
        SettingCard setting_order = new SettingCard(getContext(), Keys.option_order);
        setting_order.addChoice(new SettingCard.Choice(Keys.option_order_same, getString(R.string.option_order_01), R.drawable.option_order_same));
        setting_order.addChoice(new SettingCard.Choice(Keys.option_order_random, getString(R.string.option_order_02), R.drawable.option_order_random));
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
        setting_victory.addChoice(new SettingCard.Choice(Keys.option_victory_last, getString(R.string.option_victory_last), R.drawable.option_victory_last));
        setting_victory.addChoice(new SettingCard.Choice(Keys.option_victory_distance, getString(R.string.option_victory_dist), R.drawable.option_victory_distance));
        setting_victory.init(getString(R.string.option_victory), Keys.option_victory_last);
        settingCards.add(setting_victory);

        //collision
        SettingCard setting_collision = new SettingCard(getContext(), Keys.option_collision);
        setting_collision.addChoice(new SettingCard.Choice(Keys.option_collision_on, getString(R.string.option_collision_on), R.drawable.option_collision_on));
        setting_collision.addChoice(new SettingCard.Choice(Keys.option_collision_off, getString(R.string.option_collision_off), R.drawable.option_collision_off));
        setting_collision.init(getString(R.string.option_collision), Keys.option_collision_on);
        settingCards.add(setting_collision);

        //connections
        SettingCard setting_connections = new SettingCard(getContext(), Keys.option_connections);
        setting_connections.addChoice(new SettingCard.Choice(Keys.option_connections_4, getString(R.string.option_connections_4), R.drawable.option_connections_01));
        setting_connections.addChoice(new SettingCard.Choice(Keys.option_connections_8, getString(R.string.option_connections_8), R.drawable.option_connections_02));
        setting_connections.init(getString(R.string.option_connections), Keys.option_connections_4);
        settingCards.add(setting_connections);


        adapterOptions = new AdapterOptions(settingCards, this);

        ViewTreeObserver viewTreeObserver = binding.rvOptions.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.rvOptions.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width  = binding.rvOptions.getMeasuredWidth();
                //int height = rv_options.getMeasuredHeight();

                Log.d(TAG, "rv_options width " + width);
                int noCol = Utilities.calculateNoOfColumns(getContext(), width);

                GridLayoutManager llm_options = new NoScrollGridLayoutManager(getContext(), noCol, RecyclerView.VERTICAL, false);
                binding.rvOptions.setLayoutManager(llm_options);

            }
        });

        binding.rvOptions.setAdapter(adapterOptions);



        binding.ivPlayer.setOnTouchListener(new MyTouchListener());
        binding.ivAiEasy.setOnTouchListener(new MyTouchListener());
        binding.ivAiNormal.setOnTouchListener(new MyTouchListener());
        binding.ivAiHard.setOnTouchListener(new MyTouchListener());


        binding.gsbBack.setOnClickListener(view -> {
            getAct().onBackPressed();
        });
        binding.gsbBack.setOnTouchListener(this);

        binding.gsbPlay.setOnClickListener(view -> {
            if (getNumPlayers() < 2) {
                showDialog();
            } else {
                Bundle b = new Bundle();
                b.putSerializable("Players", adapterCarts.getSelected());
                b.putStringArrayList("Options", adapterOptions.getKeys());
                getAct().showGame(b);
            }
        });
        binding.gsbPlay.setOnTouchListener(this);

        adapterPlayers = new AdapterPlayers(getContext());
        GridLayoutManager glm_players = new GridLayoutManager(getContext(), 2);
        binding.rvPlayers.setLayoutManager(glm_players);
        binding.rvPlayers.setAdapter(adapterPlayers);

        return rootView;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                switch (v.getId()){
                    case R.id.gsb_play:
                        getAct().playSoundSign();
                        break;
                    case R.id.gsb_back:
                        getAct().playSoundSign();
                        break;

                }
                break;
        }
        return false;
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


    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                getAct().playSoundPickup();
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

        DialogFewPlayerBinding binding = DialogFewPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        dialog.setContentView(view);
        //dialog.setContentView(R.layout.dialog_few_player);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        binding.tvTitle.setTypeface(getAct().customtf_normal);
        binding.tvDescription.setTypeface(getAct().customtf_normal);

        binding.dbOk.setOnClickListener(v -> dialog.dismiss());
        binding.dbOk.setSoundListener(getAct());

        dialog.show();
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }

}
