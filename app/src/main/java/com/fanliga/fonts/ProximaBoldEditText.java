package com.fanliga.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class ProximaBoldEditText extends EditText {

    public ProximaBoldEditText(Context context) {
        super(context);
        init();
    }

    public ProximaBoldEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProximaBoldEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ProximaBoldEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "proxima-nova-bold.ttf");
            setTypeface(tf);
        }
    }
}

