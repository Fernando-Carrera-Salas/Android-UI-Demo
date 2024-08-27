package com.java.uidemo.util;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import com.java.uidemo.view.TouchableSpan;

public class LinkTouchMovementMethod extends LinkMovementMethod
{
    private TouchableSpan pressed_span;

    @Override
    public boolean onTouchEvent(TextView text_view, Spannable spannable, MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            pressed_span = getPressedSpan(text_view, spannable, event);
            if (pressed_span != null)
            {
                pressed_span.setPressed(true);
                Selection.setSelection(spannable, spannable.getSpanStart(pressed_span), spannable.getSpanEnd(pressed_span));
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            TouchableSpan touchedSpan = getPressedSpan(text_view, spannable, event);
            if (pressed_span != null && touchedSpan != pressed_span)
            {
                pressed_span.setPressed(false);
                pressed_span = null;
                Selection.removeSelection(spannable);
            }
        }
        else
        {
            if (pressed_span != null)
            {
                pressed_span.setPressed(false);
                super.onTouchEvent(text_view, spannable, event);
            }
            pressed_span = null;
            Selection.removeSelection(spannable);
        }
        return true;
    }

    private TouchableSpan getPressedSpan(TextView text_view, Spannable spannable, MotionEvent event)
    {
        int x = (int) event.getX() - text_view.getTotalPaddingLeft() + text_view.getScrollX();
        int y = (int) event.getY() - text_view.getTotalPaddingTop() + text_view.getScrollY();
        Layout layout = text_view.getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);
        TouchableSpan[] link = spannable.getSpans(position, position, TouchableSpan.class);
        TouchableSpan touchedSpan = null;
        if (link.length > 0 && positionWithinTag(position, spannable, link[0]))
        {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    private boolean positionWithinTag(int position, Spannable spannable, Object tag)
    {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
    }
}
