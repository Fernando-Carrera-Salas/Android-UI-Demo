package com.java.uidemo.view;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class TouchableSpan extends ClickableSpan
{
    private boolean is_pressed;
    private int text_color;
    private int pressed_text_color;

    public TouchableSpan(int c1, int c2)
    {
        text_color = c1;
        pressed_text_color = c2;
    }

    public void setPressed(boolean is_pressed)
    {
        this.is_pressed = is_pressed;
    }

    @Override
    public void updateDrawState(TextPaint ds)
    {
        super.updateDrawState(ds);
        ds.setColor(is_pressed ? pressed_text_color : text_color);
        ds.setStyle(Paint.Style.STROKE);
        ds.setStrokeWidth(2f);
        ds.setUnderlineText(!is_pressed);
    }
}
