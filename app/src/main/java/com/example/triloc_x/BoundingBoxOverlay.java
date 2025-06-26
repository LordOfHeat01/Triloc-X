// BoundingBoxOverlay.java
package com.example.triloc_x;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BoundingBoxOverlay extends View {
    private RectF box;
    private final Paint paint;

    public BoundingBoxOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFF00FF00); // Green
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    public void setBox(RectF rect) {
        this.box = rect;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (box != null) {
            canvas.drawRect(box, paint);
        }
    }
}
