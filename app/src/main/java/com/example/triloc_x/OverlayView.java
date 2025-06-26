package com.example.triloc_x;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {
    private RectF boundingBox;
    private Paint paint;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0x80FF0000); // semi-transparent red
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    public void setBoundingBox(RectF box) {
        this.boundingBox = box;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (boundingBox != null) {
            canvas.drawRect(boundingBox, paint);
        }
    }
}
