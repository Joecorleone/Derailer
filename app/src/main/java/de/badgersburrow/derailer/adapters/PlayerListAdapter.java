package de.badgersburrow.derailer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import de.badgersburrow.derailer.R;

import java.util.ArrayList;

/**
 * Created by cetty on 27.07.16.
 */
public class PlayerListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> playerList = new ArrayList<>();
    private ArrayList<Integer> colorList = new ArrayList<>();
    private Context context;

    public PlayerListAdapter(ArrayList<String> playerList, ArrayList<Integer> colorList, Context context){
        this.playerList = playerList;
        this.colorList = colorList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int i) {
        return playerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_players, null);
        }

        EditText playerText = (EditText) view.findViewById(R.id.list_item_string);
        playerText.setText(playerList.get(i));

        Button deleteButton = (Button) view.findViewById(R.id.bDelete);
        Button colorButton = (Button) view.findViewById(R.id.bColor);

        colorButton.setBackgroundColor(colorList.get(i));

        deleteButton.setOnClickListener(v -> {
            playerList.remove(i);
            colorList.remove(i);
            notifyDataSetChanged();
        });

        return view;
    }
}
