package org.schabi.newpipe.info_list;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.local.dialog.PlaylistAppendDialog;
import org.schabi.newpipe.player.playqueue.SinglePlayQueue;
import org.schabi.newpipe.util.NavigationHelper;
import org.schabi.newpipe.util.OnClickGesture;
import org.schabi.newpipe.util.ShareUtils;

import java.util.Collections;

import io.reactivex.disposables.Disposable;

public class InfoItemSelectedListener<T> {
    @Nullable private OnClickGesture<T> itemSelectedListener;

    @Nullable private OnClickGesture<T> playBackgroundListener;
    @Nullable private OnClickGesture<T> playPopupListener;
    @Nullable private OnClickGesture<T> playMainListener;
    @Nullable private OnClickGesture<T> downloadListener;
    @Nullable private OnClickGesture<T> addToPlaylistListener;
    @Nullable private OnClickGesture<T> shareListener;
    @Nullable private OnClickGesture<T> deleteListener;
    @Nullable private OnClickGesture<T> setAsPlaylistThumbnailListener;


    public static InfoItemSelectedListener<StreamInfoItem> buildDefaultStreamListener(FragmentActivity activity) {
        InfoItemSelectedListener<StreamInfoItem> result = new InfoItemSelectedListener<>();

        result.setDownloadListener(new OnClickGesture<StreamInfoItem>() {
            Disposable disposable;

            @Override
            public void selected(StreamInfoItem selectedItem) {
                if (disposable != null) {
                    disposable.dispose();
                }

                disposable = NavigationHelper.openDownloadDialog(activity, selectedItem, null);
            }
        });

        result.setPlayBackgroundListener(new OnClickGesture<StreamInfoItem>() {
            @Override
            public void selected(StreamInfoItem selectedItem) {
                NavigationHelper.playOnBackgroundPlayer(activity,
                        new SinglePlayQueue(selectedItem), true);
            }

            @Override
            public void held(StreamInfoItem selectedItem) {
                NavigationHelper.enqueueOnBackgroundPlayer(activity,
                        new SinglePlayQueue(selectedItem), false);
            }
        });

        result.setPlayPopupListener(new OnClickGesture<StreamInfoItem>() {
            @Override
            public void selected(StreamInfoItem selectedItem) {
                NavigationHelper.playOnPopupPlayer(activity,
                        new SinglePlayQueue(selectedItem), true);
            }

            @Override
            public void held(StreamInfoItem selectedItem) {
                NavigationHelper.enqueueOnPopupPlayer(activity,
                        new SinglePlayQueue(selectedItem), false);
            }
        });

        result.setPlayMainListener(selectedItem ->
                NavigationHelper.playOnMainPlayer(activity,
                        new SinglePlayQueue(selectedItem), true));

        result.setShareListener(selectedItem ->
                ShareUtils.shareUrl(activity,
                        selectedItem.getName(), selectedItem.getUrl()));

        result.setAddToPlaylistListener(selectedItem -> {
            if (activity.getSupportFragmentManager() != null) {
                PlaylistAppendDialog.fromStreamInfoItems(Collections.singletonList(selectedItem))
                        .show(activity.getSupportFragmentManager(), InfoItemSelectedListener.class.getSimpleName());
            }
        });

        return result;
    }


    @Nullable
    public OnClickGesture<T> getItemSelectedListener() {
        return itemSelectedListener;
    }

    @Nullable
    public OnClickGesture<T> getPlayBackgroundListener() {
        return playBackgroundListener;
    }

    @Nullable
    public OnClickGesture<T> getPlayPopupListener() {
        return playPopupListener;
    }

    @Nullable
    public OnClickGesture<T> getPlayMainListener() {
        return playMainListener;
    }

    @Nullable
    public OnClickGesture<T> getDownloadListener() {
        return downloadListener;
    }

    @Nullable
    public OnClickGesture<T> getAddToPlaylistListener() {
        return addToPlaylistListener;
    }

    @Nullable
    public OnClickGesture<T> getShareListener() {
        return shareListener;
    }

    @Nullable
    public OnClickGesture<T> getDeleteListener() {
        return deleteListener;
    }

    @Nullable
    public OnClickGesture<T> getSetAsPlaylistThumbnailListener() {
        return setAsPlaylistThumbnailListener;
    }


    public void setItemSelectedListener(@Nullable OnClickGesture<T> itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public void setPlayBackgroundListener(@Nullable OnClickGesture<T> playBackgroundListener) {
        this.playBackgroundListener = playBackgroundListener;
    }

    public void setPlayPopupListener(@Nullable OnClickGesture<T> playPopupListener) {
        this.playPopupListener = playPopupListener;
    }

    public void setPlayMainListener(@Nullable OnClickGesture<T> playMainListener) {
        this.playMainListener = playMainListener;
    }

    public void setDownloadListener(@Nullable OnClickGesture<T> downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void setAddToPlaylistListener(@Nullable OnClickGesture<T> addToPlaylistListener) {
        this.addToPlaylistListener = addToPlaylistListener;
    }

    public void setShareListener(@Nullable OnClickGesture<T> shareListener) {
        this.shareListener = shareListener;
    }

    public void setDeleteListener(@Nullable OnClickGesture<T> deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setSetAsPlaylistThumbnailListener(@Nullable OnClickGesture<T> setAsPlaylistThumbnailListener) {
        this.setAsPlaylistThumbnailListener = setAsPlaylistThumbnailListener;
    }
}
