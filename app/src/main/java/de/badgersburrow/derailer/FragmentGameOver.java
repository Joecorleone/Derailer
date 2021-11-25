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

import de.badgersburrow.derailer.objects.PlayerResult;
import de.badgersburrow.derailer.objects.PlayerSelection;

public class FragmentGameOver extends Fragment implements OnClickListener{

    ArrayList<PlayerResult> players;
    ArrayList<PlayerSelection> playersSelection;
    ArrayList<String> options;
    int connections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_game_over, container, false);

        Bundle b = getArguments();

        playersSelection = (ArrayList<PlayerSelection>)b.getSerializable("PlayersSelection");
        players = (ArrayList<PlayerResult>)b.getSerializable("Players");
        options = b.getStringArrayList("Options");
        connections = b.getInt("connections");

        int selectedThemeId = getAct().SP.getInt("theme",0);

        TextView tv_header = rootView.findViewById(R.id.tv_header);
        tv_header.setTypeface(getAct().customtf_normal);

        // podium
        TextView tv_place1_dist = rootView.findViewById(R.id.tv_place1_dist);
        TextView tv_place2_dist = rootView.findViewById(R.id.tv_place2_dist);
        TextView tv_place3_dist = rootView.findViewById(R.id.tv_place3_dist);
        TextView tv_place1_label = rootView.findViewById(R.id.tv_place1_label);
        TextView tv_place2_label = rootView.findViewById(R.id.tv_place2_label);
        TextView tv_place3_label = rootView.findViewById(R.id.tv_place3_label);
        ImageView iv_place1_main = rootView.findViewById(R.id.iv_place1_main);
        ImageView iv_place2_main = rootView.findViewById(R.id.iv_place2_main);
        ImageView iv_place3_main = rootView.findViewById(R.id.iv_place3_main);
        ImageView iv_place1_color = rootView.findViewById(R.id.iv_place1_color);
        ImageView iv_place2_color = rootView.findViewById(R.id.iv_place2_color);
        ImageView iv_place3_color = rootView.findViewById(R.id.iv_place3_color);

        List<TextView> list_tv_dist = Arrays.asList(tv_place1_dist, tv_place2_dist, tv_place3_dist);
        List<TextView> list_tv_label = Arrays.asList(tv_place1_label, tv_place2_label, tv_place3_label);
        List<ImageView> list_iv_main = Arrays.asList(iv_place1_main, iv_place2_main, iv_place3_main);
        List<ImageView> list_iv_color = Arrays.asList(iv_place1_color, iv_place2_color, iv_place3_color);

        if (options.contains(Keys.option_victory_last)){
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
            LinearLayoutCompat ll_place3_podium = rootView.findViewById(R.id.ll_place3_podium);
            ll_place3_podium.setVisibility(View.INVISIBLE);
        }

        Button bReplay = rootView.findViewById(R.id.bNewTry);
        Button bExit = rootView.findViewById(R.id.bMainMenu);
        bReplay.setOnClickListener(this);
        bExit.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bNewTry:
                Bundle b = new Bundle();
                b.putSerializable("Players", playersSelection);
                b.putStringArrayList("Options", options);
                b.putInt("connections", connections);
                getAct().showGame(b);
                break;
            case R.id.bMainMenu:
                getAct().onBackPressed();
                break;
        }
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }

}