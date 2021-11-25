package de.badgersburrow.derailer.objects;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import de.badgersburrow.derailer.R;

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
        TypedArray titles = context.getResources().obtainTypedArray(R.array.theme_titles);
        String s = titles.getString(this.id);
        titles.recycle();
        return s;
    }

    /*public Drawable getForgroundResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.typeImages);
        return typeImages.getDrawable(this.id);
    }*/

    public Drawable getCartResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.carts);
        Drawable d = typeImages.getDrawable(this.id);
        typeImages.recycle();
        return d;
    }

    public Drawable getCartColorResId(Context context) {
        TypedArray typeImages = context.getResources().obtainTypedArray(R.array.carts_color);
        Drawable d = typeImages.getDrawable(this.id);
        typeImages.recycle();
        return d;
    }

    public int getThemeColorResId(Context context) {
        TypedArray typeColors = context.getResources().obtainTypedArray(R.array.theme_colors);
        int c = typeColors.getColor(this.id,0);
        typeColors.recycle();
        return c;
    }

    public Drawable getBackgroundResId(Context context) {
        TypedArray typeBackgrounds = context.getResources().obtainTypedArray(R.array.theme_backgrounds);
        Drawable d = typeBackgrounds.getDrawable(this.id);
        typeBackgrounds.recycle();
        return d;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
}
