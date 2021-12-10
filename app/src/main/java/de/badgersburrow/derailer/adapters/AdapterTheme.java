package de.badgersburrow.derailer.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.ActivityMain;
import de.badgersburrow.derailer.FragmentMain;
import de.badgersburrow.derailer.R;
import de.badgersburrow.derailer.databinding.ItemSpacerBinding;
import de.badgersburrow.derailer.databinding.ItemThemeBinding;
import de.badgersburrow.derailer.objects.Theme;

import java.util.ArrayList;


public class AdapterTheme extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String LOG_TAG = "AdapterFridge";
    private ArrayList<Theme> mThemes;
    private Context mContext;
    ThemeListener listener;

    public static class Spacer extends RecyclerView.ViewHolder{
        ItemSpacerBinding binding;

        public Spacer(ItemSpacerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        ItemThemeBinding binding;

        public DataObjectHolder(ItemThemeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterTheme(Context mContext, ArrayList<Theme> mThemes, ThemeListener listener) {
        this.mContext = mContext;
        this.mThemes = mThemes;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        if (viewType == 0){
            return new Spacer(ItemSpacerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        ItemThemeBinding binding = ItemThemeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DataObjectHolder(binding);
    }

    @Override
    public int getItemViewType(int pos){
        if (pos == 0){
            return 0;
        }
        return 1;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DataObjectHolder){
            DataObjectHolder objectHolder = (DataObjectHolder) holder;
            int themeId = position-1;
            Theme theme = mThemes.get(themeId);
            objectHolder.binding.llTheme.setTag(themeId);
            objectHolder.binding.tvTitle.setText(theme.getTitle(mContext));
            objectHolder.binding.tvTitle.setTypeface(ActivityMain.customtf_normal);
            objectHolder.binding.ivStart.setImageDrawable(theme.getBackgroundResId(mContext));
            objectHolder.binding.llThemeBg.setBackground(theme.getBackgroundResId(mContext));
            objectHolder.binding.ivTheme.setImageDrawable(theme.getCartResId(mContext));
            int color = theme.getThemeColorResId(mContext);
            Drawable drawable_color = theme.getCartColorResId(mContext);
            drawable_color.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            objectHolder.binding.ivThemeColor.setImageDrawable(drawable_color);
            if (theme.isSelected()){
                objectHolder.binding.ivSelect.setVisibility(View.VISIBLE);
            } else {
                objectHolder.binding.ivSelect.setVisibility(View.INVISIBLE);
            }
            objectHolder.binding.llTheme.setOnClickListener(v -> this.listener.selectTheme(themeId));
        }
    }

    public void update(ArrayList<Theme> themes) {
        mThemes.clear();
        mThemes.addAll(themes);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mThemes.size() + 1;
    }

    public interface ThemeListener{
        void selectTheme(int pos);
    }

}
