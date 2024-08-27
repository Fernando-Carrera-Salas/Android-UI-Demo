package com.java.uidemo.util;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.java.uidemo.R;


public class CustomFocusChangeListener implements View.OnFocusChangeListener
{
    private View[] views;
    private View[] button_views;
    private FocusListener listener;

    public CustomFocusChangeListener()
    {
        this.listener = null;
    }

    public CustomFocusChangeListener(FocusListener listener)
    {
        this.listener = listener;
    }

    public void setViews(View[] views, View[] button_views)
    {
        this.views = views;
        this.button_views = button_views;
    }
    @Override
    public void onFocusChange(View view, boolean b)
    {
        try
        {
            if (b)
            {
                ((EditText)view).setError(null);
                ((EditText)view).setTextColor(ContextCompat.getColor(view.getContext(), R.color.dark_gray));
            }
            else
            {
                ((EditText)view).setTextColor(ContextCompat.getColor(view.getContext(), R.color.light_gray));
            }
            if (views!=null)
            {
                for (View v : views)
                {
                    if (v instanceof TextView)
                    {
                        if (b)
                        {
                            ((TextView)v).setTextColor(ContextCompat.getColor(view.getContext(), R.color.dark_gray));
                        }
                        else
                        {
                            ((TextView)v).setTextColor(ContextCompat.getColor(view.getContext(), R.color.light_gray));
                        }
                    }
                    else if (v instanceof ImageView)
                    {
                        if (b)
                        {
                            ((ImageView)v).setColorFilter(ContextCompat.getColor(view.getContext(), R.color.dark_gray));
                        }
                        else
                        {
                            ((ImageView)v).setColorFilter(ContextCompat.getColor(view.getContext(), R.color.light_gray));
                        }
                    }
                }
            }
            if (button_views!=null)
            {
                for (View v : button_views)
                {
                    if (v instanceof TextView)
                    {
                        if (b)
                        {
                            ((TextView)v).setTextColor(ContextCompat.getColorStateList(view.getContext(), R.color.dark_gray_to_light_gray));
                            v.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ((TextView)v).setTextColor(ContextCompat.getColor(view.getContext(), R.color.light_gray));
                            v.setVisibility(View.INVISIBLE);
                        }
                    }
                    else if (v instanceof ImageView)
                    {
                        if (b)
                        {
                            ((ImageView)v).setImageTintList(ContextCompat.getColorStateList(view.getContext(), R.color.dark_gray_to_light_gray));
                            v.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ((ImageView)v).setImageTintList(ContextCompat.getColorStateList(view.getContext(), R.color.light_gray_to_white));
                            v.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (listener!=null)
            listener.onFocusChange(b);
    }

    public interface FocusListener
    {
        void onFocusChange(boolean focused);
    }
}
