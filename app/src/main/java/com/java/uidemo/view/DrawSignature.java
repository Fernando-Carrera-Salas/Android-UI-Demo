package com.java.uidemo.view;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;

/**
 * A very simple drawing canvas with just one color and fixed brush size. Ideal for signatures.
 */
public class DrawSignature extends View
{
    private static final float STROKE_WIDTH = 5f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private final Paint paint = new Paint();
    private final Path path = new Path();

    private float last_touch_x;
    private float last_touch_y;
    private final RectF dirty_rect = new RectF();

    public DrawSignature(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
    }


    public void clear()
    {
        path.reset();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                last_touch_x = x;
                last_touch_y = y;
                return true;

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

                resetDirtyRect(x, y);
                int history_size = event.getHistorySize();
                for (int i = 0; i < history_size; i++) {
                    float h_x = event.getHistoricalX(i);
                    float h_y = event.getHistoricalY(i);
                    expandDirtyRect(h_x, h_y);
                    path.lineTo(h_x, h_y);
                }
                path.lineTo(x, y);
                break;

            default:
                return false;
        }

        invalidate((int) (dirty_rect.left - HALF_STROKE_WIDTH),
                (int) (dirty_rect.top - HALF_STROKE_WIDTH),
                (int) (dirty_rect.right + HALF_STROKE_WIDTH),
                (int) (dirty_rect.bottom + HALF_STROKE_WIDTH));

        last_touch_x = x;
        last_touch_y = y;

        return true;
    }

    private void expandDirtyRect(float h_x, float h_y)
    {
        if (h_x < dirty_rect.left) {
            dirty_rect.left = h_x;
        } else if (h_x > dirty_rect.right) {
            dirty_rect.right = h_x;
        }

        if (h_y < dirty_rect.top) {
            dirty_rect.top = h_y;
        } else if (h_y > dirty_rect.bottom) {
            dirty_rect.bottom = h_y;
        }
    }

    private void resetDirtyRect(float eventX, float eventY)
    {
        dirty_rect.left = Math.min(last_touch_x, eventX);
        dirty_rect.right = Math.max(last_touch_x, eventX);
        dirty_rect.top = Math.min(last_touch_y, eventY);
        dirty_rect.bottom = Math.max(last_touch_y, eventY);
    }

}