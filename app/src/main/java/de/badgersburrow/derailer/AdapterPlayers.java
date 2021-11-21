package de.badgersburrow.derailer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

            Matrix m = new Matrix();
            if (key.equals(Keys.player)){
                m.postScale(0.3f,0.3f);
            } else {
                m.postScale(0.6f,0.6f);
            }

            Bitmap icon = Utilities.drawableToBitmap(mContext.getResources().getDrawable(resIds.get(key)));
            int p_width = icon.getWidth();
            int p_height = icon.getHeight();

            holder.iv_icon.setImageBitmap(Bitmap.createBitmap(icon , 0, 0, p_width, p_height, m, true));
            holder.tv_num.setText(String.valueOf(player_num.get(key)));
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

    private int getPosition(String keySearch){
        int count = 0;
        for (String key : order){
            if (player_num.containsKey(key)){
                if (key.equals(keySearch)){
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public void addPlayer(String key){
        if (player_num.containsKey(key)){
            int pos = getPosition(key);
            player_num.put(key, player_num.get(key)+1);
            notifyItemChanged(pos);
        } else {
            player_num.put(key, 1);
            int pos = getPosition(key);
            notifyItemInserted(pos);
        }
    }

    public void removePlayer(String key){
        if (player_num.containsKey(key)){
            int pos = getPosition(key);

            if (player_num.get(key) == 1){
                player_num.remove(key);
                notifyItemRemoved(pos);
            } else {
                player_num.put(key, player_num.get(key)-1);
                notifyItemChanged(pos);
            }
        }
    }

}
