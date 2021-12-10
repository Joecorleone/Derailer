package de.badgersburrow.derailer.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.ActivityMain;
import de.badgersburrow.derailer.FragmentMain;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.databinding.ItemCartBinding;
import de.badgersburrow.derailer.databinding.ItemOptionBinding;
import de.badgersburrow.derailer.views.SettingCard;

import java.util.ArrayList;

/**
 * Created by cetty on 27.07.16.
 */
public class AdapterOptions extends RecyclerView.Adapter<AdapterOptions.DataObjectHolder> {

    private ArrayList<SettingCard> settingCards = new ArrayList<>();
    SoundListener listener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        ItemOptionBinding binding;

        public DataObjectHolder(ItemOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterOptions(ArrayList<SettingCard> settingCards, SoundListener listener) {
        this.settingCards = settingCards;
        this.listener = listener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemOptionBinding binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.binding.llOption.setTag(position);
        SettingCard settingCard = settingCards.get(position);

        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL);
        settingCard.setPadding(0,settingCard.getTopPadding(),0,0); // necessary
        settingCard.setSoundListener(listener);
        holder.binding.llOption.addView(settingCard, llparams);

        holder.binding.tvOption.setText(settingCard.getTitle());
        holder.binding.tvOption.setTypeface(ActivityMain.customtf_normal);
    }

    public void enable(String key){
        for (int i=0; i<settingCards.size(); i++){
            SettingCard card = settingCards.get(i);
            if (card.getKey().equals(key)){
                card.setEnabled(true);
            }
        }
    }

    public void disable(String key){
        for (int i=0; i<settingCards.size(); i++){
            SettingCard card = settingCards.get(i);
            if (card.getKey().equals(key)){
                card.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return settingCards.size();
    }

    public ArrayList<String> getKeys(){
        ArrayList<String> keys = new ArrayList<>();
        for (SettingCard card : settingCards){
            if (card.isEnabled()){
                keys.add(card.getKeyChosen());
            } else {
                keys.add(card.getKeyDefault());
            }
        }
        return keys;
    }

    public interface SoundListener{
        void playSoundOptionChanged();
    }
}
