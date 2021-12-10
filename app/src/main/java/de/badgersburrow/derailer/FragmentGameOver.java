package de.badgersburrow.derailer;

/**
 * Created by cetty on 15.01.17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.badgersburrow.derailer.databinding.FragmentGameBinding;
import de.badgersburrow.derailer.databinding.FragmentGameOverBinding;
import de.badgersburrow.derailer.objects.PlayerResult;
import de.badgersburrow.derailer.objects.PlayerSelection;
import de.badgersburrow.derailer.views.GameButton;

public class FragmentGameOver extends Fragment implements OnClickListener{

    FragmentGameOverBinding binding;

    ArrayList<PlayerResult> players;
    ArrayList<PlayerSelection> playersSelection;
    ArrayList<String> options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGameOverBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();

        //View rootView = inflater.inflate(R.layout.fragment_game_over, container, false);

        Bundle b = getArguments();

        playersSelection = (ArrayList<PlayerSelection>)b.getSerializable("PlayersSelection");
        players = (ArrayList<PlayerResult>)b.getSerializable("Players");
        options = b.getStringArrayList("Options");

        int selectedThemeId = getAct().SP.getInt("theme",0);

        binding.tvHeader.setTypeface(getAct().customtf_normal);

        // podium

        List<TextView> list_tv_dist = Arrays.asList(binding.tvPlace1Dist, binding.tvPlace2Dist, binding.tvPlace3Dist);
        List<TextView> list_tv_label = Arrays.asList(binding.tvPlace1Label, binding.tvPlace2Label, binding.tvPlace3Label);
        List<ImageView> list_iv_main = Arrays.asList(binding.ivPlace1Main, binding.ivPlace2Main, binding.ivPlace3Main);
        List<ImageView> list_iv_color = Arrays.asList(binding.ivPlace1Color, binding.ivPlace2Color, binding.ivPlace3Color);

        if (options.contains(Keys.option_victory_last)){
            binding.tvPlace1Dist.setVisibility(View.INVISIBLE);
            binding.tvPlace2Dist.setVisibility(View.INVISIBLE);
            binding.tvPlace3Dist.setVisibility(View.INVISIBLE);

            Collections.sort(players,
                    (o1, o2) -> -Integer.compare(o1.getOutCount(), o2.getOutCount()));
        } else {
            Collections.sort(players,
                    (o1, o2) -> -Integer.compare(o1.getTileCount(), o2.getTileCount()));
        }

        Matrix m = new Matrix();
        m.postRotate(270);

        for (int i=0; i<3 && i<players.size(); i++){
            PlayerResult p = players.get(i);
            list_tv_dist.get(i).setTypeface(getAct().customtf_normal);
            list_tv_dist.get(i).setText(String.valueOf(p.getTileCount()));
            list_tv_label.get(i).setTypeface(getAct().customtf_normal);
            list_tv_label.get(i).setText(p.getLabel(getContext()));
            list_tv_label.get(i).setTextColor(p.getColor());

            Bitmap bmpColor = p.getBmpColor(getContext(), selectedThemeId);
            int width = bmpColor.getWidth();
            int height = bmpColor.getHeight();
            list_iv_color.get(i).setImageBitmap(Bitmap.createBitmap(bmpColor , 0, 0, width, height, m, true));
            list_iv_color.get(i).setColorFilter(p.getColor());
            list_iv_main.get(i).setImageBitmap(Bitmap.createBitmap(p.getBmpMain(getContext(), selectedThemeId) , 0, 0, width, height, m, true));
        }

        if (players.size() < 3){
            binding.llPlace3Podium.setVisibility(View.INVISIBLE);
        }

        binding.gbNewtry.setOnClickListener(this);
        binding.gbNewtry.setSoundListener(getAct());

        binding.gbMenu.setOnClickListener(this);
        binding.gbMenu.setSoundListener(getAct());

        getAct().playSoundVictory();

        return rootView;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gb_newtry:
                Bundle b = new Bundle();
                b.putSerializable("Players", playersSelection);
                b.putStringArrayList("Options", options);
                getAct().showGame(b);
                break;
            case R.id.gb_menu:
                getAct().onBackPressed();
                break;
        }
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }

}