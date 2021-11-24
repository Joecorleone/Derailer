package de.badgersburrow.derailer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.adapters.AdapterTheme;
import de.badgersburrow.derailer.objects.Theme;
import de.badgersburrow.derailer.views.GameSignButton;

import java.util.ArrayList;


/**
 * Created by reim on 27.11.16.
 */

public class ThemeActivity extends Activity {
    TextView tv_header;
    RecyclerView rvTheme;
    SharedPreferences SP;
    SharedPreferences.Editor SPE;
    Context mContext;
    ArrayList<Theme> themes;
    AdapterTheme mAdapter;

    int selectedThemeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Utilities.FullScreencall(this);

        mContext = this.getApplicationContext();
        SP = PreferenceManager.getDefaultSharedPreferences(this);
        selectedThemeId = SP.getInt("theme",0);

        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_header.setTypeface(MainActivity.customtf_normal);

        SPE = SP.edit();

        GameSignButton gsb_back = findViewById(R.id.gsb_back);
        gsb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // populate theme arraylist
        themes = new ArrayList<>();
        themes.add(new Theme(0, selectedThemeId==0));
        themes.add(new Theme(1, selectedThemeId==1));
        themes.add(new Theme(2, selectedThemeId==2));
        themes.add(new Theme(3, selectedThemeId==3));


        rvTheme = (RecyclerView) findViewById(R.id.rv_history);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvTheme.setLayoutManager(llm);

        //registerForContextMenu(rv_history);
        mAdapter = new AdapterTheme(this, themes);
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
    }

    public void selectTheme(View view) {
        int position = (int) view.getTag();
        Log.d("test","pos:" + view.getTag() + ", type:" + themes.get(position).getTitle(mContext));
        selectedThemeId = position;
        SPE.putInt("theme",position);
        SPE.commit();

        for (int i = 0; i < themes.size(); i++){
            themes.get(i).setSelected(position == i);
        }
        mAdapter.notifyDataSetChanged();
    }

}
