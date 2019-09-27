package org.schabi.newpipe.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class ToolbarBelowItemAnimation extends Animation {
    private View toolbar;
    private LinearLayout.LayoutParams toolbarLayoutParams;
    private int marginStart, marginEnd;

    /**
     * Initialize the animation
     * @param duration The duration of the animation, in milliseconds
     * @param toolbar The toolbar to animate
     */
    public ToolbarBelowItemAnimation(int duration, View toolbar) {
        setDuration(duration);
        this.toolbar = toolbar;

        toolbarLayoutParams = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
        marginStart = toolbarLayoutParams.bottomMargin;
        marginEnd = (marginStart == 0 ? -(toolbar.getHeight() + toolbarLayoutParams.topMargin) : 0);

        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        toolbarLayoutParams.bottomMargin = marginStart
                + (int) ((marginEnd - marginStart) * interpolatedTime);

        if (marginStart == 0) {
            toolbar.setAlpha(1.0f - interpolatedTime);
            if (interpolatedTime >= 1.0f) {
                toolbar.setVisibility(View.GONE);
            }
        } else {
            toolbar.setAlpha(interpolatedTime);
            if (interpolatedTime >= 1.0f) {
                toolbar.setVisibility(View.VISIBLE);
            }
        }

        toolbar.requestLayout();
    }

    public static void resetToolbarBelowItem(View toolbar) {
        toolbar.setVisibility(View.GONE);

        LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) toolbar.getLayoutParams());
        if (layoutParams.bottomMargin == 0) {
            layoutParams.bottomMargin = -(toolbar.getHeight() + layoutParams.topMargin);
        }
    }
}