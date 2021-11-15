package de.badgersburrow.derailer;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.badgersburrow.derailer.objects.Theme;

import java.util.ArrayList;


public class AdapterTheme extends RecyclerView.Adapter<AdapterTheme.DataObjectHolder> {

    private static String LOG_TAG = "AdapterFridge";
    private ArrayList<Theme> mThemes;
    private static Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_theme, rl_theme_bg;
        ImageView iv_select;
        TextView tv_title;
        ImageView iv_theme;
        ImageView iv_theme_color;

        public DataObjectHolder(View itemView) {
            super(itemView);
            rl_theme = (RelativeLayout) itemView.findViewById(R.id.rl_theme);
            rl_theme_bg = (RelativeLayout) itemView.findViewById(R.id.rl_theme_bg);
            iv_select = (ImageView) itemView.findViewById(R.id.iv_select);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_theme = (ImageView) itemView.findViewById(R.id.iv_theme);
            iv_theme_color = (ImageView) itemView.findViewById(R.id.iv_theme_color);
        }
    }

    public AdapterTheme(Context mContext, ArrayList<Theme> mThemes) {
        this.mContext = mContext;
        this.mThemes = mThemes;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.rl_theme.setTag(position);
        holder.tv_title.setText(mThemes.get(position).getTitle(mContext));
        holder.tv_title.setTypeface(MainActivity.customtf_normal);
        holder.rl_theme_bg.setBackground(mThemes.get(position).getBackgroundResId(mContext));
        holder.iv_theme.setImageDrawable(mThemes.get(position).getCartResId(mContext));
        int color = mThemes.get(position).getThemeColorResId(mContext);
        Drawable drawable_color = mThemes.get(position).getCartColorResId(mContext);
        drawable_color.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        holder.iv_theme_color.setImageDrawable(drawable_color);
        if (mThemes.get(position).isSelected()){
            holder.iv_select.setImageResource(R.drawable.select_train);
        } else {
            holder.iv_select.setImageResource(R.drawable.select_not);
        }
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

}
