package com.exsample.maria.mobi2.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.WindowInsets;

public class StretchView extends android.support.v7.widget.AppCompatImageView {

    public StretchView(Context context) {
        super(context);
    }

    public StretchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StretchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean fitSystemWindows(Rect insets) {
        int statusBar = insets.left;
        return true;
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        int statusBar = insets.getSystemWindowInsetBottom();
        return super.dispatchApplyWindowInsets(insets);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int statusBar = insets.getSystemWindowInsetBottom();
        return super.onApplyWindowInsets(insets);
    }
}
