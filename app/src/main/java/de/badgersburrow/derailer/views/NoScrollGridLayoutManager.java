package de.badgersburrow.derailer.views;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class NoScrollGridLayoutManager extends GridLayoutManager {
    public NoScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    // it will always pass false to RecyclerView when calling "canScrollVertically()" method.
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
