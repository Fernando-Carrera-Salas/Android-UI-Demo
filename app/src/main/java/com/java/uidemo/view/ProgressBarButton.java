package com.java.uidemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

/**
 * A long press button with a {@link ProgressBar} display.
 * </p>
 * Since it is based on a {@link RelativeLayout} and the views must be passed through {@link #setup}, its design is very flexible.
 */
public class ProgressBarButton extends RelativeLayout
{
    private int current_progress;
    private boolean reversed_progress;
    private ValueAnimator animator;
    private ProgressBar pb_progress_bar;
    private int max_progress;
    private long press_duration;
    private ProgressBarButtonEventListener listener;
    private boolean cancelled;

    public ProgressBarButton(Context context)
    {
        super(context);
        listener = null;
    }

    public ProgressBarButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        listener = null;
    }

    public ProgressBarButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        listener = null;
    }

    public int getCurrent_progress()
    {
        return current_progress;
    }

    public int getMax_progress()
    {
        return max_progress;
    }

    public long getPress_duration()
    {
        return press_duration;
    }

    public void resetProgress()
    {
        try
        {
            cancelled = true;
            current_progress = 0;
            pb_progress_bar.setProgress(0);
            animator.cancel();
            reversed_progress = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setup(ProgressBar progress_bar, View button_view, long press_duration)
    {
        this.max_progress = progress_bar.getMax();
        this.press_duration = press_duration;
        this.pb_progress_bar = progress_bar;

        animator = ValueAnimator.ofInt(0, max_progress);
        animator.setDuration(press_duration);
        animator.addUpdateListener(valueAnimator ->
        {
            int new_progress = (Integer) valueAnimator.getAnimatedValue();
            if (!(current_progress<10&&new_progress>=max_progress))
            {
                if (new_progress<current_progress)
                    reversed_progress = true;
                else if (new_progress>current_progress)
                    reversed_progress = false;
                this.pb_progress_bar.setProgress(new_progress);
                if (new_progress==0&&current_progress>0)
                {
                    if (reversed_progress)
                    {
                        animator.reverse();
                        reversed_progress = false;
                    }
                    animator.end();
                }
                current_progress = new_progress;
                if (listener!=null)
                    listener.onProgressUpdate(current_progress);
            }
        });
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(@NonNull Animator animation, boolean reverse)
            {
                super.onAnimationEnd(animation);
                if (progress_bar.getProgress()==max_progress)
                {
                    current_progress = 0;
                    progress_bar.setProgress(0);
                    if (listener!=null&&!cancelled)
                        listener.onEnd();
                }
                else
                {
                    if (progress_bar.getProgress()==0)
                    {
                        if (reverse)
                            animator.reverse();
                        reversed_progress = false;
                    }
                }
            }
        });
        button_view.setClickable(true);
        button_view.setFocusable(true);
        button_view.setOnTouchListener(new OnTouchListener()
        {
            private boolean moved_outside;
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    cancelled = false;
                    moved_outside = false;
                    if (reversed_progress)
                    {
                        animator.reverse();
                        reversed_progress = false;
                    }
                    if (animator.isStarted())
                        animator.resume();
                    else
                        animator.start();
                    if (listener!=null)
                        listener.onPress();
                }
                else
                {
                    boolean cancel = false;
                    if (event.getAction()==MotionEvent.ACTION_UP
                            ||event.getAction()==MotionEvent.ACTION_CANCEL
                            ||event.getAction()==MotionEvent.ACTION_OUTSIDE)
                    {
                        cancel = true;
                        moved_outside = false;
                    }
                    if (event.getAction()==MotionEvent.ACTION_MOVE&&!moved_outside)
                    {
                        if (!inViewInBounds(ProgressBarButton.this,(int)event.getRawX(),(int)event.getRawY()))
                        {
                            cancel = true;
                            moved_outside = true;
                        }
                    }
                    if (cancel)
                    {
                        if (!reversed_progress&& progress_bar.getProgress()>0&&animator.isStarted())
                        {
                            animator.reverse();
                            reversed_progress = true;
                        }
                        else
                        {
                            if (progress_bar.getProgress()==0)
                            {
                                animator.end();
                            }
                        }
                        if (listener!=null)
                            listener.onCancel();
                    }
                }
                return false;
            }
        });
    }

    public ProgressBarButtonEventListener getListener()
    {
        return listener;
    }

    public void setListener(ProgressBarButtonEventListener listener)
    {
        this.listener = listener;
    }

    private boolean inViewInBounds(View view, int x, int y)
    {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }




    public interface ProgressBarButtonEventListener
    {
        void onProgressUpdate(int progress);

        void onPress();

        void onCancel();

        void onEnd();

    }
}
