package com.example.cetty.derailer.objects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.example.cetty.derailer.R;

/**
 * Created by reim on 03.01.17.
 */

public class Theme {
    int id;
    boolean selected;

    public Theme(int id, boolean selected) {
        this.id = id;
        this.selected = selected;
    }

    public String getTitle(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.theme_titles);
        return titles[this.id];
    }

    /*public Drawable getForgroundResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.typeImages);
        return typeImages.getDrawable(this.id);
    }*/

    public Drawable getCartResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.carts);
        return typeImages.getDrawable(this.id);
    }

    public Drawable getCartColorResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.carts_color);
        return typeImages.getDrawable(this.id);
    }

    public int getThemeColorResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.theme_colors);
        return typeImages.getColor(this.id,0);
    }

    public Drawable getBackgroundResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.theme_backgrounds);
        return typeImages.getDrawable(this.id);
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
