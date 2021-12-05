package de.badgersburrow.derailer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.badgersburrow.derailer.Keys;
import de.badgersburrow.derailer.R;

/**
 * Created by cetty on 27.07.16.
 */
public class AdapterOptionsInfo extends RecyclerView.Adapter<AdapterOptionsInfo.DataObjectHolder> {

    private ArrayList<String> options = new ArrayList<>();

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        ImageView iv_option;

        public DataObjectHolder(View itemView) {
            super(itemView);
            iv_option = itemView.findViewById(R.id.iv_option);
        }
    }

    public AdapterOptionsInfo(ArrayList<String> options) {
        this.options = options;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option_info, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        String key = options.get(position);
        switch(key){
            case Keys.option_obstacle_none:
                holder.iv_option.setImageResource(R.drawable.option_obstacle_none);
                break;
            case Keys.option_obstacle_few:
                holder.iv_option.setImageResource(R.drawable.option_obstacle_few);
                break;
            case Keys.option_obstacle_many:
                holder.iv_option.setImageResource(R.drawable.option_obstacle_many);
                break;
            /*case Keys.option_daynight_01:
                holder.iv_option.setImageResource(R.drawable.option_obstacle_none);
                break;
            case Keys.option_daynight_02:
                holder.iv_option.setImageResource(R.drawable.option_day);
                break;*/
            case Keys.option_draw_fill:
                holder.iv_option.setImageResource(R.drawable.option_draw_fill);
                break;
            case Keys.option_draw_new:
                holder.iv_option.setImageResource(R.drawable.option_draw_new);
                break;
            case Keys.option_order_same:
                holder.iv_option.setImageResource(R.drawable.option_order_same);
                break;
            case Keys.option_order_random:
                holder.iv_option.setImageResource(R.drawable.option_order_random);
                break;
            case Keys.option_suddendeath_01:
                holder.iv_option.setImageResource(R.drawable.option_suddendeath_off);
                break;
            case Keys.option_suddendeath_02:
                holder.iv_option.setImageResource(R.drawable.option_suddendeath_on);
                break;
            case Keys.option_victory_last:
                holder.iv_option.setImageResource(R.drawable.option_victory_last);
                break;
            case Keys.option_victory_distance:
                holder.iv_option.setImageResource(R.drawable.option_victory_distance);
                break;
            case Keys.option_collision_on:
                holder.iv_option.setImageResource(R.drawable.option_collision_on);
                break;
            case Keys.option_collision_off:
                holder.iv_option.setImageResource(R.drawable.option_collision_off);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return options.size();
    }
}
