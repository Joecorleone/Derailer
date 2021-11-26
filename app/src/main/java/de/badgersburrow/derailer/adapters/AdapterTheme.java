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
import de.badgersburrow.derailer.objects.Theme;

import java.util.ArrayList;


public class AdapterTheme extends RecyclerView.Adapter<AdapterTheme.DataObjectHolder> {

    private static String LOG_TAG = "AdapterFridge";
    private ArrayList<Theme> mThemes;
    private Context mContext;
    ThemeListener listener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        LinearLayoutCompat ll_theme, ll_theme_bg;
        ImageView iv_start, iv_select;
        TextView tv_title;
        ImageView iv_theme;
        ImageView iv_theme_color;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ll_theme = (LinearLayoutCompat) itemView.findViewById(R.id.ll_theme);
            ll_theme_bg = (LinearLayoutCompat) itemView.findViewById(R.id.ll_theme_bg);
            iv_start = (ImageView) itemView.findViewById(R.id.iv_start);
            iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_theme = (ImageView) itemView.findViewById(R.id.iv_theme);
            iv_theme_color = (ImageView) itemView.findViewById(R.id.iv_theme_color);
        }
    }

    public AdapterTheme(Context mContext, ArrayList<Theme> mThemes, ThemeListener listener) {
        this.mContext = mContext;
        this.mThemes = mThemes;
        this.listener = listener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.ll_theme.setTag(position);
        holder.tv_title.setText(mThemes.get(position).getTitle(mContext));
        holder.tv_title.setTypeface(ActivityMain.customtf_normal);
        holder.iv_start.setImageDrawable(mThemes.get(position).getBackgroundResId(mContext));
        holder.ll_theme_bg.setBackground(mThemes.get(position).getBackgroundResId(mContext));
        holder.iv_theme.setImageDrawable(mThemes.get(position).getCartResId(mContext));
        int color = mThemes.get(position).getThemeColorResId(mContext);
        Drawable drawable_color = mThemes.get(position).getCartColorResId(mContext);
        drawable_color.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        holder.iv_theme_color.setImageDrawable(drawable_color);
        if (mThemes.get(position).isSelected()){
            holder.iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.iv_select.setVisibility(View.INVISIBLE);
        }
        holder.ll_theme.setOnClickListener(v -> this.listener.selectTheme(position));
    }

    public void update(ArrayList<Theme> themes) {
        mThemes.clear();
        mThemes.addAll(themes);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    public interface ThemeListener{
        void selectTheme(int pos);
    }

}
