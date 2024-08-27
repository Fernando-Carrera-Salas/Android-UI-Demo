package com.java.uidemo.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public abstract class Animations
{
    public static void slideUpIn(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                view.getHeight(),
                0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    public static void slideDownIn(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                view.getHeight()*-1,
                0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    public static void slideDownOut(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                view.getHeight());
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }
    public static void slideRightIn(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                view.getWidth(),
                0,
                0,
                0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }
    public static void slideRightOut(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                view.getWidth(),
                0,
                0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }
    public static void slideLeftOut(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                view.getWidth()*-1,
                0,
                0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }
    public static void slideLeftIn(final View view, final int duration){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                view.getWidth()*-1,
                0,
                0,
                0);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }
    public static void fadeIn(final View view, final int duration, final AnimationsListener listener)
    {
        view.setVisibility(View.VISIBLE);
        AlphaAnimation animate = new AlphaAnimation(0f,1f);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (listener!=null)
                    listener.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }
    public static void fadeOut(final View view, final int duration, final AnimationsListener listener)
    {
        view.setVisibility(View.VISIBLE);
        AlphaAnimation animate = new AlphaAnimation(1f,0f);
        animate.setDuration(duration);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
                if (listener!=null)
                    listener.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    public static void close(View view, long duration, AnimationsListener listener)
    {
        view.setVisibility(View.VISIBLE);
        ValueAnimator va = ValueAnimator.ofFloat(view.getHeight(),0);
        va.setDuration(duration);
        va.addUpdateListener(valueAnimator ->
        {
            float value = (Float) valueAnimator.getAnimatedValue();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.height = (int) value;
            view.setLayoutParams(params);
        });
        va.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                view.setVisibility(View.INVISIBLE);
                if (listener!=null)
                    listener.onAnimationEnd();
                super.onAnimationEnd(animation);
            }
        });

        va.start();
    }

    public static void open(View view, long duration, AnimationsListener listener, int height)
    {
        view.setVisibility(View.VISIBLE);
        ValueAnimator va = ValueAnimator.ofFloat(0,height);
        va.setDuration(duration);
        va.addUpdateListener(valueAnimator ->
        {
            float value = (Float) valueAnimator.getAnimatedValue();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.height = (int) value;
            view.setLayoutParams(params);
        });
        va.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if (listener!=null)
                    listener.onAnimationEnd();
                super.onAnimationEnd(animation);
            }
        });

        va.start();
    }

    public static void spin(final View view, final int duration)
    {
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(duration);
        view.startAnimation(rotate);
    }

    public static void scaleUpToSize(final View view, final int duration, float size)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(view.getHeight(), size);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(animation ->
        {
            float y = (Float) animation.getAnimatedValue();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            params.height = (int) y;
            view.setLayoutParams(params);
        });
        valueAnimator.start();
    }

    public static void bounce(final View view, int repeat_times, AnimationsListener listener)
    {
        view.setVisibility(View.INVISIBLE);
        int height = ((View)view.getParent()).getHeight();
        final TranslateAnimation translate1 = new TranslateAnimation(
                0,
                0,
                0,
                height/2f-view.getHeight()/2f);
        translate1.setDuration(300);

        final TranslateAnimation translate2 = new TranslateAnimation(
                0,
                0,
                0,
                (height/2f-view.getHeight()/2f)*-1);
        translate2.setDuration(375);
        translate2.setStartOffset(600);

        final ScaleAnimation scale1 = new ScaleAnimation(1f, 1f/0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 2.5f);
        scale1.setDuration(200);
        scale1.setStartOffset(225);

        final ScaleAnimation scale2 = new ScaleAnimation(1f, 0.8f, 1f, 1f/0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.75f);
        scale2.setDuration(200);
        scale2.setStartOffset(525);

        final AnimationSet as = new AnimationSet(false);
        as.addAnimation(translate1);
        as.addAnimation(scale1);
        as.addAnimation(translate2);
        as.addAnimation(scale2);

        as.setAnimationListener(new Animation.AnimationListener()
        {
            private int repeat_count = 0;
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (repeat_count<=repeat_times)
                {
                    view.startAnimation(as);
                    repeat_count++;
                }
                else if (listener!=null)
                {
                    view.clearAnimation();
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        view.startAnimation(as);
        view.setVisibility(View.VISIBLE);
    }

    public static void spin(View view, boolean clock, AnimationsListener listener)
    {
        int degrees = 360;
        if (!clock)
            degrees = -360;
        RotateAnimation rotate = new RotateAnimation(
                0, degrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(900);
        rotate.setRepeatCount(1);
        rotate.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if (listener!=null)
                {
                    view.clearAnimation();
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        view.startAnimation(rotate);
    }

    public static void fadeOutScaleUp(final View view, final long duration, float from, float to, AnimationsListener listener)
    {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation scale = new ScaleAnimation(from,to,from,to,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(duration);
        AlphaAnimation fade = new AlphaAnimation(1f,0f);
        fade.setDuration(duration);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(scale);
        as.addAnimation(fade);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
                if (listener!=null)
                    listener.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(as);
    }

    public static void fadeInScaleDown(final View view, final long duration, float from, float to, AnimationsListener listener)
    {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation scale = new ScaleAnimation(from,to,from,to,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(duration);
        AlphaAnimation fade = new AlphaAnimation(0f,1f);
        fade.setDuration(duration);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(scale);
        as.addAnimation(fade);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                if (listener!=null)
                    listener.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(as);
    }

    public static void ripple(final View view, final long duration)
    {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation scale = new ScaleAnimation(0.5f,1,0.5f,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setFillAfter(false);
        scale.setDuration(duration);
        AlphaAnimation fade = new AlphaAnimation(0.75f,0f);
        fade.setDuration(duration);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(scale);
        as.addAnimation(fade);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(as);
    }

    public interface AnimationsListener
    {
        void onAnimationEnd();
    }
}
