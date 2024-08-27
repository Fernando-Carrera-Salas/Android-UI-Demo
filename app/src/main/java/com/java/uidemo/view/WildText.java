package com.java.uidemo.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * A simple, fun implementation of {@link com.daimajia.androidanimations.library}, which creates a waving white text with a black outline.
 */
public class WildText extends LinearLayout
{
    public WildText(Context context) {
        super(context);
    }

    public WildText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WildText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text, Typeface tf)
    {
        char[] cs = text.toCharArray();
        for (char c : cs)
        {
            RelativeLayout rl = new RelativeLayout(getContext());
            TextView tv_shadow = new TextView(getContext());
            tv_shadow.setText(String.valueOf(c));
            tv_shadow.setTextColor(Color.BLACK);
            tv_shadow.setTypeface(tf);
            tv_shadow.setShadowLayer(5, 0, 0, Color.BLACK);
            tv_shadow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            tv_shadow.setPadding(5,5,5,5);
            rl.addView(tv_shadow);
            TextView tv_text = new TextView(getContext());
            tv_text.setText(String.valueOf(c));
            tv_text.setTextColor(Color.WHITE);
            tv_text.setTypeface(tf);
            tv_text.setShadowLayer(5, 0, 0, Color.WHITE);
            tv_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
            tv_text.setPadding(5,5,5,5);
            rl.addView(tv_text);
            addView(rl);
        }
    }
    public void startAnim()
    {
        for(int index = 0; index < getChildCount(); index++)
        {
            View v = getChildAt(index);
            YoYo.with(Techniques.Wave).duration(5000).delay(index*50L).repeat(YoYo.INFINITE).playOn(v);
            YoYo.with(Techniques.Swing).duration(3000).delay(index*50L).repeat(YoYo.INFINITE).playOn(v);
            YoYo.with(Techniques.Shake).duration(2000).delay(index*50L).repeat(YoYo.INFINITE).playOn(v);
            YoYo.with(Techniques.Pulse).duration(1000).delay(index*50L).repeat(YoYo.INFINITE).playOn(v);
            YoYo.with(Techniques.Wobble).duration(2000).delay(index*100L).repeat(YoYo.INFINITE).playOn(v);
        }
        setVisibility(View.VISIBLE);
    }
}
