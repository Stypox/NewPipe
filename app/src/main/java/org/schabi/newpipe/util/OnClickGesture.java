package org.schabi.newpipe.util;

import android.support.v7.widget.RecyclerView;

public interface OnClickGesture<T> {

    void selected(T selectedItem);

    default void held(T selectedItem) {
        // Optionally override
    }

    default void drag(T selectedItem, RecyclerView.ViewHolder viewHolder) {
        // Optionally override
    }
}
