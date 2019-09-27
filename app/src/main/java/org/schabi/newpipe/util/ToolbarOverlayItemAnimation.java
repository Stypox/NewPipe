package org.schabi.newpipe.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ToolbarOverlayItemAnimation extends Animation {
    private static final float minimumUndergroundAlpha = 0.4f;

    private View toolbar;
    private View[] viewsUnderground;
    private boolean goingToShowToolbar;

    /**
     * Initialize the animation
     * @param duration The duration of the animation, in milliseconds
     * @param toolbar The view to animate
     * @param viewsUnderground The views under the toolbar to hide when the toolbar is shown and vice versa
     */
    public ToolbarOverlayItemAnimation(int duration, View toolbar, View... viewsUnderground) {
        setDuration(duration);
        this.toolbar = toolbar;
        this.viewsUnderground = viewsUnderground;

        goingToShowToolbar = (toolbar.getVisibility() != View.VISIBLE);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (goingToShowToolbar) {
            toolbar.setAlpha(interpolatedTime);
            for (View view : viewsUnderground) {
                view.setAlpha(1.0f - (1.0f - minimumUndergroundAlpha) * interpolatedTime);
            }

            if (interpolatedTime >= 1.0f) {
                toolbar.setVisibility(View.VISIBLE);
                toolbar.setClickable(true); // to prevent clicks on the underground views
            }
        } else {
            toolbar.setAlpha(1.0f - interpolatedTime);
            for (View view : viewsUnderground) {
                view.setAlpha(minimumUndergroundAlpha + (1.0f - minimumUndergroundAlpha) * interpolatedTime);
            }

            if (interpolatedTime >= 1.0f) {
                resetToolbarOverlayItem(toolbar, viewsUnderground);
            }
        }
    }

    public static void resetToolbarOverlayItem(View toolbar, View... viewsUnderground) {
        toolbar.setVisibility(View.INVISIBLE);
        toolbar.setClickable(false);
        for (View view : viewsUnderground) {
            view.setAlpha(1.0f);
            view.setVisibility(View.VISIBLE);
        }
    }
}