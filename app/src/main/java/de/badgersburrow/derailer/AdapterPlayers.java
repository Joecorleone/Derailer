package de.badgersburrow.derailer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cetty on 27.07.16.
 */
public class AdapterPlayers extends RecyclerView.Adapter<AdapterPlayers.DataObjectHolder> {


    private HashMap<String, Integer> player_num;
    private static ArrayList<String> order = new ArrayList<>();
    static{
        order.add(Keys.player);
        order.add(Keys.aiEasy);
        order.add(Keys.aiNormal);
        order.add(Keys.aiHard);
    }
    private static HashMap<String, Integer> resIds = new HashMap<>();
    static {
        resIds.put(Keys.player, R.drawable.player_icon);
        resIds.put(Keys.aiEasy, R.drawable.ai_easy_icon);
        resIds.put(Keys.aiNormal, R.drawable.ai_normal_icon);
        resIds.put(Keys.aiHard, R.drawable.ai_hard_icon);
    }
    private Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_num;


        public DataObjectHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_num = itemView.findViewById(R.id.tv_num);
        }
    }

    public AdapterPlayers(Context mContext) {
        this.mContext = mContext;
        this.player_num = new HashMap<>();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        String key = getKey(position);
        if (key != null){
            holder.iv_icon.setImageResource(resIds.get(key));
            holder.tv_num.setText(player_num.get(key));
            holder.tv_num.setTypeface(MainActivity.customtf_normal);
        }
    }

    public void update(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return player_num.size();
    }

    private String getKey(int position){
        int count = 0;
        for (String key : order){
            if (player_num.containsKey(key)){
                if (count == position){
                    return key;
                }
                count++;
            }
        }
        return null;
    }

    public void addPlayer(String key){
        if (player_num.containsKey(key)){
            player_num.put(key, player_num.get(key)+1);
        } else {
            player_num.put(key, 1);
        }
    }

    public void removePlayer(String key){
        if (player_num.containsKey(key)){
            if (player_num.get(key) == 1){
                player_num.remove(key);
            } else {
                player_num.put(key, player_num.get(key)-1);
            }
        }
    }

}
