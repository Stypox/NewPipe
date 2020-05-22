package org.schabi.newpipe.local.holder;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.schabi.newpipe.R;
import org.schabi.newpipe.database.LocalItem;
import org.schabi.newpipe.database.playlist.PlaylistStreamEntry;
import org.schabi.newpipe.database.stream.model.StreamStateEntity;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.info_list.ItemHolderUtils;
import org.schabi.newpipe.info_list.ItemSelectedListener;
import org.schabi.newpipe.local.LocalItemBuilder;
import org.schabi.newpipe.local.history.HistoryRecordManager;
import org.schabi.newpipe.util.AnimationUtils;
import org.schabi.newpipe.util.ImageDisplayConstants;
import org.schabi.newpipe.util.Localization;
import org.schabi.newpipe.util.OnClickGesture;
import org.schabi.newpipe.views.AnimatedProgressBar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LocalPlaylistStreamItemHolder extends LocalItemHolder {

    public final ImageView itemThumbnailView;
    public final TextView itemVideoTitleView;
    public final TextView itemAdditionalDetailsView;
    public final TextView itemDurationView;
    public final View itemHandleView;
    public final AnimatedProgressBar itemProgressView;
    @NonNull public final LinearLayout itemToolbarView;

    LocalPlaylistStreamItemHolder(LocalItemBuilder infoItemBuilder, int layoutId, ViewGroup parent) {
        super(infoItemBuilder, layoutId, parent);

        itemThumbnailView = itemView.findViewById(R.id.itemThumbnailView);
        itemVideoTitleView = itemView.findViewById(R.id.itemVideoTitleView);
        itemAdditionalDetailsView = itemView.findViewById(R.id.itemAdditionalDetails);
        itemDurationView = itemView.findViewById(R.id.itemDurationView);
        itemHandleView = itemView.findViewById(R.id.itemHandle);
        itemProgressView = itemView.findViewById(R.id.itemProgressView);
        itemToolbarView = ItemHolderUtils.getToolbarViewFromItem(itemView);
    }

    public LocalPlaylistStreamItemHolder(LocalItemBuilder infoItemBuilder, ViewGroup parent) {
        this(infoItemBuilder, R.layout.list_stream_playlist_item, parent);
    }

    @Override
    public void updateFromItem(final LocalItem localItem, HistoryRecordManager historyRecordManager, final DateFormat dateFormat) {
        if (!(localItem instanceof PlaylistStreamEntry)) return;
        final PlaylistStreamEntry item = (PlaylistStreamEntry) localItem;

        itemVideoTitleView.setText(item.title);
        itemAdditionalDetailsView.setText(Localization.concatenateStrings(item.uploader,
                NewPipe.getNameOfService(item.serviceId)));

        if (item.duration > 0) {
            itemDurationView.setText(Localization.getDurationString(item.duration));
            itemDurationView.setBackgroundColor(ContextCompat.getColor(itemBuilder.getContext(),
                    R.color.duration_background_color));
            itemDurationView.setVisibility(View.VISIBLE);

            StreamStateEntity state = historyRecordManager.loadLocalStreamStateBatch(new ArrayList<LocalItem>() {{ add(localItem); }}).blockingGet().get(0);
            if (state != null) {
                itemProgressView.setVisibility(View.VISIBLE);
                itemProgressView.setMax((int) item.duration);
                itemProgressView.setProgress((int) TimeUnit.MILLISECONDS.toSeconds(state.getProgressTime()));
            } else {
                itemProgressView.setVisibility(View.GONE);
            }
        } else {
            itemDurationView.setVisibility(View.GONE);
        }

        // Default thumbnail is shown on error, while loading and if the url is empty
        itemBuilder.displayImage(item.thumbnailUrl, itemThumbnailView,
                ImageDisplayConstants.DISPLAY_THUMBNAIL_OPTIONS);

        ItemHolderUtils.resetItemToolbarView(itemToolbarView, itemThumbnailView, itemDurationView, itemProgressView);
        itemView.setOnClickListener(v -> toggleItemToolbar());

        ItemSelectedListener<LocalItem> listener = itemBuilder.getStreamSelectedListener();

        ItemHolderUtils.setListenersOrHideToolbarButtons(listener, item, itemView);
        ItemHolderUtils.setClickListener(listener, item, itemThumbnailView);
        ItemHolderUtils.setLongClickListener(listener, item, itemView, itemThumbnailView);

        ItemHolderUtils.setDragListener(listener, item, this, () -> {
            if (itemToolbarView.getVisibility() == View.VISIBLE) {
                toggleItemToolbar();
            }
        }, itemHandleView);
    }

    @Override
    public void updateState(LocalItem localItem, HistoryRecordManager historyRecordManager) {
        if (!(localItem instanceof PlaylistStreamEntry)) return;
        final PlaylistStreamEntry item = (PlaylistStreamEntry) localItem;

        StreamStateEntity state = historyRecordManager.loadLocalStreamStateBatch(new ArrayList<LocalItem>() {{ add(localItem); }}).blockingGet().get(0);
        if (state != null && item.duration > 0) {
            itemProgressView.setMax((int) item.duration);
            if (itemProgressView.getVisibility() == View.VISIBLE) {
                itemProgressView.setProgressAnimated((int) TimeUnit.MILLISECONDS.toSeconds(state.getProgressTime()));
            } else {
                itemProgressView.setProgress((int) TimeUnit.MILLISECONDS.toSeconds(state.getProgressTime()));
                AnimationUtils.animateView(itemProgressView, true, 500);
            }
        } else if (itemProgressView.getVisibility() == View.VISIBLE) {
            AnimationUtils.animateView(itemProgressView, false, 500);
        }
    }

    private void toggleItemToolbar() {
        ItemHolderUtils.toggleItemToolbar(itemToolbarView,
                itemThumbnailView, itemDurationView, itemProgressView);
    }
}
