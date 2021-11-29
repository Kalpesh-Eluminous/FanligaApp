package com.fanliga.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ProximaLightTextView extends TextView {

    public ProximaLightTextView(Context context) {
        super(context);
        init();
    }

    public ProximaLightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaLightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProximaLightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "proxima-nova-light.ttf");
            setTypeface(tf);
        }
    }
}

