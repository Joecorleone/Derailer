package de.badgersburrow.derailer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.adapters.AdapterTheme;
import de.badgersburrow.derailer.objects.Theme;
import de.badgersburrow.derailer.views.GameSignButton;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by reim on 27.11.16.
 */

public class FragmentThemeSelection extends Fragment implements AdapterTheme.ThemeListener{
    TextView tv_header;
    RecyclerView rvTheme;
    Context mContext;
    ArrayList<Theme> themes;
    AdapterTheme mAdapter;

    int selectedThemeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_theme_selection, container, false);

        selectedThemeId = getAct().SP.getInt("theme",0);

        tv_header = rootView.findViewById(R.id.tv_header);
        tv_header.setTypeface(getAct().customtf_normal);

        GameSignButton gsb_back = rootView.findViewById(R.id.gsb_back);
        gsb_back.setOnClickListener(view -> {
            getAct().playSoundSign();
            getAct().onBackPressed();
        });


        // populate theme arraylist
        themes = new ArrayList<>();
        themes.add(new Theme(0, selectedThemeId==0));
        themes.add(new Theme(1, selectedThemeId==1));
        themes.add(new Theme(2, selectedThemeId==2));
        themes.add(new Theme(3, selectedThemeId==3));


        rvTheme = rootView.findViewById(R.id.rv_history);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvTheme.setLayoutManager(llm);

        //registerForContextMenu(rv_history);
        mAdapter = new AdapterTheme(getContext(), themes, this);
        rvTheme.setAdapter(mAdapter);

        /*sTheme = (Spinner) findViewById(R.id.sTheme);
        sTheme.setSelection(selectedTheme);
        sTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPE.putInt("theme",position);
                SPE.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        return rootView;
    }

    @Override
    public void selectTheme(int position) {
        Log.d("test","pos:" + position + ", type:" + themes.get(position).getTitle(requireContext()));
        selectedThemeId = position;
        getAct().SPE.putInt("theme",position);
        getAct().SPE.commit();

        for (int i = 0; i < themes.size(); i++){
            themes.get(i).setSelected(position == i);
        }
        mAdapter.notifyDataSetChanged();
    }

    ActivityMain getAct(){
        return (ActivityMain) getActivity();
    }

}
