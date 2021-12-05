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


public class AdapterTheme extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String LOG_TAG = "AdapterFridge";
    private ArrayList<Theme> mThemes;
    private Context mContext;
    ThemeListener listener;

    public static class Spacer extends RecyclerView.ViewHolder{


        public Spacer(View itemView) {
            super(itemView);

        }
    }

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        if (viewType == 0){
            return new Spacer(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spacer, parent, false));
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);

        return new DataObjectHolder(view);
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
        if (holder.getItemViewType()!=0){
            int themeId = position-1;
            Theme theme = mThemes.get(themeId);
            DataObjectHolder objectHolder = (DataObjectHolder) holder;
            objectHolder.ll_theme.setTag(themeId);
            objectHolder.tv_title.setText(theme.getTitle(mContext));
            objectHolder.tv_title.setTypeface(ActivityMain.customtf_normal);
            objectHolder.iv_start.setImageDrawable(theme.getBackgroundResId(mContext));
            objectHolder.ll_theme_bg.setBackground(theme.getBackgroundResId(mContext));
            objectHolder.iv_theme.setImageDrawable(theme.getCartResId(mContext));
            int color = theme.getThemeColorResId(mContext);
            Drawable drawable_color = theme.getCartColorResId(mContext);
            drawable_color.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            objectHolder.iv_theme_color.setImageDrawable(drawable_color);
            if (theme.isSelected()){
                objectHolder.iv_select.setVisibility(View.VISIBLE);
            } else {
                objectHolder.iv_select.setVisibility(View.INVISIBLE);
            }
            objectHolder.ll_theme.setOnClickListener(v -> this.listener.selectTheme(themeId));
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
