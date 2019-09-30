package org.schabi.newpipe.info_list;

import android.support.annotation.Nullable;
import android.view.View;

import org.schabi.newpipe.R;
import org.schabi.newpipe.util.OnClickGesture;

public class InfoItemToolbarHelper {
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

    public static <T> void setListenersOrHideToolbarButtons(View itemView, InfoItemSelectedListener<T> listener, T item) {
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
}
