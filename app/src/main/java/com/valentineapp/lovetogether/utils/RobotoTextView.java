package com.valentineapp.lovetogether.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lequy on 3/14/2017.
 */

public class RobotoTextView extends TextView {
    public RobotoTextView(Context context) {
        super(context);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        this.setTypeface(typeface);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        this.setTypeface(typeface);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        this.setTypeface(typeface);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
