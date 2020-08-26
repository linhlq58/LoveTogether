package com.valentineapp.lovetogether.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lequy on 2/8/2017.
 */

public class FuturaTextView extends TextView {
    public FuturaTextView(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura.ttf");
        this.setTypeface(typeface);
    }

    public FuturaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura.ttf");
        this.setTypeface(typeface);
    }

    public FuturaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Futura.ttf");
        this.setTypeface(typeface);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
