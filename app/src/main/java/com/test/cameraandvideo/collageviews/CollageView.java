package com.test.cameraandvideo.collageviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;


public class CollageView extends android.support.v7.widget.AppCompatImageView {

    private static final int PADDING = 8;
    private static final float STROKE_WIDTH = 8.0f;

    private Paint mBorderPaint;

    public CollageView(Context context) {
        this(context, null);
    }

    public CollageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setPadding(PADDING, PADDING, PADDING, PADDING);
    }

    public CollageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}