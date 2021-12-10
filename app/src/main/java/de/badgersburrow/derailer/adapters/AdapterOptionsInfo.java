package de.badgersburrow.derailer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.badgersburrow.derailer.Keys;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.databinding.ItemOptionBinding;
import de.badgersburrow.derailer.databinding.ItemOptionInfoBinding;

/**
 * Created by cetty on 27.07.16.
 */
public class AdapterOptionsInfo extends RecyclerView.Adapter<AdapterOptionsInfo.DataObjectHolder> {

    private ArrayList<String> options = new ArrayList<>();

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        ItemOptionInfoBinding binding;

        public DataObjectHolder(ItemOptionInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterOptionsInfo(ArrayList<String> options) {
        this.options = options;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemOptionInfoBinding binding = ItemOptionInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        String key = options.get(position);
        switch(key){
            case Keys.option_obstacle_none:
                holder.binding.ivOption.setImageResource(R.drawable.option_obstacle_none);
                break;
            case Keys.option_obstacle_few:
                holder.binding.ivOption.setImageResource(R.drawable.option_obstacle_few);
                break;
            case Keys.option_obstacle_many:
                holder.binding.ivOption.setImageResource(R.drawable.option_obstacle_many);
                break;
            /*case Keys.option_daynight_01:
                holder.iv_option.setImageResource(R.drawable.option_obstacle_none);
                break;
            case Keys.option_daynight_02:
                holder.iv_option.setImageResource(R.drawable.option_day);
                break;*/
            case Keys.option_draw_fill:
                holder.binding.ivOption.setImageResource(R.drawable.option_draw_fill);
                break;
            case Keys.option_draw_new:
                holder.binding.ivOption.setImageResource(R.drawable.option_draw_new);
                break;
            case Keys.option_order_same:
                holder.binding.ivOption.setImageResource(R.drawable.option_order_same);
                break;
            case Keys.option_order_random:
                holder.binding.ivOption.setImageResource(R.drawable.option_order_random);
                break;
            case Keys.option_suddendeath_01:
                holder.binding.ivOption.setImageResource(R.drawable.option_suddendeath_off);
                break;
            case Keys.option_suddendeath_02:
                holder.binding.ivOption.setImageResource(R.drawable.option_suddendeath_on);
                break;
            case Keys.option_victory_last:
                holder.binding.ivOption.setImageResource(R.drawable.option_victory_last);
                break;
            case Keys.option_victory_distance:
                holder.binding.ivOption.setImageResource(R.drawable.option_victory_distance);
                break;
            case Keys.option_collision_on:
                holder.binding.ivOption.setImageResource(R.drawable.option_collision_on);
                break;
            case Keys.option_collision_off:
                holder.binding.ivOption.setImageResource(R.drawable.option_collision_off);
                break;
            case Keys.option_connections_4:
                holder.binding.ivOption.setImageResource(R.drawable.option_connections_01);
                break;
            case Keys.option_connections_8:
                holder.binding.ivOption.setImageResource(R.drawable.option_connections_02);
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
