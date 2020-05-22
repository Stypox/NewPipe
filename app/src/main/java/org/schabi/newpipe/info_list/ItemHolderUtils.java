package org.schabi.newpipe.info_list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import org.schabi.newpipe.R;
import org.schabi.newpipe.util.OnClickGesture;
import org.schabi.newpipe.util.ToolbarBelowItemAnimation;
import org.schabi.newpipe.util.ToolbarOverlayItemAnimation;

public class ItemHolderUtils {
    private static final int toggleItemToolbarAnimationDuration = 100; // ms

    private static <T> void setListenerOrHide(View button, @Nullable OnClickGesture<T> onClickGesture, T item) {
        if (onClickGesture == null) {
            button.setVisibility(View.GONE);
        } else {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> onClickGesture.selected(item));

            button.setLongClickable(true);
            button.setOnLongClickListener(v -> {
                onClickGesture.held(item);
                return true;
            });
        }
    }

    public static <T> void setListenersOrHideToolbarButtons(ItemSelectedListener<T> listener, T item, View itemView) {
        View shareButton                  = itemView.findViewById(R.id.shareToolbarButton);
        View addToPlaylistButton          = itemView.findViewById(R.id.addToPlaylistToolbarButton);
        View deleteButton                 = itemView.findViewById(R.id.deleteToolbarButton);
        View setAsPlaylistThumbnailButton = itemView.findViewById(R.id.setAsPlaylistThumbnailToolbarButton);
        View downloadButton               = itemView.findViewById(R.id.downloadToolbarButton);
        View playBackgroundButton         = itemView.findViewById(R.id.playBackgroundToolbarButton);
        View playPopupButton              = itemView.findViewById(R.id.playPopupToolbarButton);
        View playMainButton               = itemView.findViewById(R.id.playMainToolbarButton);

        setListenerOrHide(shareButton,                  listener.getShareListener(),                  item);
        setListenerOrHide(addToPlaylistButton,          listener.getAddToPlaylistListener(),          item);
        setListenerOrHide(deleteButton,                 listener.getDeleteListener(),                 item);
        setListenerOrHide(setAsPlaylistThumbnailButton, listener.getSetAsPlaylistThumbnailListener(), item);
        setListenerOrHide(downloadButton,               listener.getDownloadListener(),               item);
        setListenerOrHide(playBackgroundButton,         listener.getPlayBackgroundListener(),         item);
        setListenerOrHide(playPopupButton,              listener.getPlayPopupListener(),              item);
        setListenerOrHide(playMainButton,               listener.getPlayMainListener(),               item);
    }

    public static <T> void setClickListener(ItemSelectedListener<T> listener, T item, View... views) {
        for(View view : views) {
            view.setOnClickListener(v -> {
                final OnClickGesture<T> itemClickedListener = listener.getItemClickedListener();
                if (itemClickedListener != null) {
                    itemClickedListener.selected(item);
                }
            });
        }
    }

    public static <T> void setLongClickListener(ItemSelectedListener<T> listener, T item, View... views) {
        for(View view : views) {
            view.setLongClickable(true);
            view.setOnLongClickListener(v -> {
                final OnClickGesture<T> itemClickedListener = listener.getItemClickedListener();
                if (itemClickedListener != null) {
                    itemClickedListener.held(item);
                }
                return true;
            });
        }
    }

    public static <T> void setDragListener(ItemSelectedListener<T> listener, T item, RecyclerView.ViewHolder viewHolder, Runnable actionOnDragStart, View... views) {
        for(View view : views) {
            view.setOnTouchListener((v, motionEvent) -> {
                view.performClick();
                OnClickGesture<T> itemClickedListener = listener.getItemClickedListener();
                if (itemClickedListener != null && motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    actionOnDragStart.run();
                    itemClickedListener.drag(item, viewHolder);
                }
                return false;
            });
        }
    }

    @NonNull
    public static LinearLayout getToolbarViewFromItem(View itemView) {
        LinearLayout itemToolbarView = itemView.findViewById(R.id.toolbarBelowItem);
        if (itemToolbarView == null) {
            itemToolbarView = itemView.findViewById(R.id.toolbarOverlayItem);
            if (itemToolbarView == null) {
                throw new IllegalArgumentException("Invalid itemView passed to getToolbarViewFromItem(): no item toolbars found");
            }
        }
        return itemToolbarView;
    }

    public static void resetItemToolbarView(LinearLayout itemToolbarView, View... viewsUnderground) {
        switch (itemToolbarView.getId()) {
            case R.id.toolbarOverlayItem:
                ToolbarOverlayItemAnimation.resetToolbarOverlayItem(itemToolbarView, viewsUnderground);
                break;
            case R.id.toolbarBelowItem:
                ToolbarBelowItemAnimation.resetToolbarBelowItem(itemToolbarView);
                break;
            default:
                throw new IllegalArgumentException("Invalid itemToolbarView passed to resetItemToolbarView()");
        }
    }

    public static void toggleItemToolbar(LinearLayout itemToolbarView, View... viewsUnderground) {
        Animation animation;
        switch (itemToolbarView.getId()) {
            case R.id.toolbarOverlayItem:
                animation = new ToolbarOverlayItemAnimation(toggleItemToolbarAnimationDuration, itemToolbarView, viewsUnderground);
                break;
            case R.id.toolbarBelowItem:
                animation = new ToolbarBelowItemAnimation(toggleItemToolbarAnimationDuration, itemToolbarView);
                break;
            default:
                throw new IllegalArgumentException("Invalid itemToolbarView passed to toggleItemToolbar()");
        }
        itemToolbarView.startAnimation(animation);
    }
}
