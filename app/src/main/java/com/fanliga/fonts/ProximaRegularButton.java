package com.fanliga.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.Nullable;

public class ProximaRegularButton extends Button {

    public ProximaRegularButton(Context context) {
        super(context);
        init();
    }

    public ProximaRegularButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaRegularButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProximaRegularButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "proxima-nova-regular.ttf");
            setTypeface(tf);
        }
    }
}

