package org.schabi.newpipe.info_list;

import org.schabi.newpipe.extractor.InfoItem;

public class DisplayedInfoItem {
    private final InfoItem infoItem;
    private boolean inSelection;

    public DisplayedInfoItem(final InfoItem infoItem) {
        this.infoItem = infoItem;
        this.inSelection = false;
    }

    public boolean isInSelection() {
        return inSelection;
    }

    public InfoItem getInfoItem() {
        return infoItem;
    }

    public void setInSelection(boolean inSelection) {
        this.inSelection = inSelection;
    }
}
