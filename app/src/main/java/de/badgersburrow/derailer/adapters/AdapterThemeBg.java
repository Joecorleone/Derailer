package de.badgersburrow.derailer.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.badgersburrow.derailer.ActivityMain;
import de.badgersburrow.derailer.databinding.ItemSpacerBinding;
import de.badgersburrow.derailer.databinding.ItemThemeBgBinding;
import de.badgersburrow.derailer.databinding.ItemThemeBinding;
import de.badgersburrow.derailer.databinding.ItemUpcomingIndicatorBinding;
import de.badgersburrow.derailer.objects.Theme;


public class AdapterThemeBg extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String LOG_TAG = "AdapterThemeBg";
    private Context mContext;

    public static class Spacer extends RecyclerView.ViewHolder{
        ItemThemeBgBinding binding;

        public Spacer(ItemThemeBgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterThemeBg(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        return new Spacer(ItemThemeBgBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    } // +1 for top spacer +2 for upcoming themes

}
