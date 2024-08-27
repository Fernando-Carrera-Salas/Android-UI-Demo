package com.java.uidemo.demos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.java.uidemo.R;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.DoubleArrayEvaluator;
import com.java.uidemo.util.Util;

/**
 * This {@link DemoActivity} displays six buttons.
 * </p><p>
 * When pressed, each button plays a different animation. Each animation uses different techniques and classes.
 */
public class AnimationActivity extends DemoActivity
{
    private boolean spin_anim;
    private AnimatorListenerAdapter spin_adapter;
    private boolean b_shape;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        initDemo();




        final RelativeLayout rl_bt_bounce = findViewById(R.id.rl_bt_bounce_animation);
        final ImageView iv_bt_bounce = findViewById(R.id.iv_bt_bounce_animation);
        rl_bt_bounce.setOnClickListener(view ->
        {
            iv_bt_bounce.setVisibility(View.INVISIBLE);
            rl_bt_bounce.setClickable(false);
            Animations.bounce(rl_bt_bounce,  3, () ->
            {
                iv_bt_bounce.setVisibility(View.VISIBLE);
                rl_bt_bounce.setClickable(true);
            });
        });




        final RelativeLayout rl_bt_spin = findViewById(R.id.rl_bt_spin_animation);
        final ImageView iv_bt_spin = findViewById(R.id.iv_bt_spin_animation);
        final RelativeLayout rl_spin_1 = findViewById(R.id.rl_spin_1_animation);
        final RelativeLayout rl_spin_2 = findViewById(R.id.rl_spin_2_animation);
        final RelativeLayout rl_spin_3 = findViewById(R.id.rl_spin_3_animation);
        final RelativeLayout rl_spin_4 = findViewById(R.id.rl_spin_4_animation);
        spin_anim = false;
        rl_bt_spin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spin_anim = true;
                iv_bt_spin.setVisibility(View.INVISIBLE);
                rl_bt_spin.setClickable(false);
                ValueAnimator va = ValueAnimator.ofFloat(0,40);
                va.setDuration(500);
                final ValueAnimator.AnimatorUpdateListener spin_listener = valueAnimator ->
                {
                    float f = ((Float) valueAnimator.getAnimatedValue());
                    float f0 = Util.convertDpToPixel(60f - f,AnimationActivity.this);
                    float f1 = Util.convertDpToPixel(0f + f,AnimationActivity.this);
                    float f2 = Util.convertDpToPixel(20f + f,AnimationActivity.this);
                    float f3 = Util.convertDpToPixel(40f + f,AnimationActivity.this);
                    float f4 = Util.convertDpToPixel(60f + f,AnimationActivity.this);

                    RelativeLayout.LayoutParams params0 = (RelativeLayout.LayoutParams) rl_bt_spin.getLayoutParams();
                    params0.height = (int) f0;
                    params0.width = (int) f0;
                    rl_bt_spin.setLayoutParams(params0);

                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rl_spin_1.getLayoutParams();
                    params1.height = (int) f1;
                    params1.width = (int) f1;
                    rl_spin_1.setLayoutParams(params1);

                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) rl_spin_2.getLayoutParams();
                    params2.height = (int) f2;
                    params2.width = (int) f2;
                    rl_spin_2.setLayoutParams(params2);

                    RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) rl_spin_3.getLayoutParams();
                    params3.height = (int) f3;
                    params3.width = (int) f3;
                    rl_spin_3.setLayoutParams(params3);

                    RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) rl_spin_4.getLayoutParams();
                    params4.height = (int) f4;
                    params4.width = (int) f4;
                    rl_spin_4.setLayoutParams(params4);
                };
                va.addUpdateListener(spin_listener);
                spin_adapter = new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        if (!spin_anim)
                        {
                            iv_bt_spin.setVisibility(View.VISIBLE);
                            rl_bt_spin.setClickable(true);
                        }
                        else
                        {
                            Animations.spin(rl_spin_1,true,null);
                            Animations.spin(rl_spin_2,false,null);
                            Animations.spin(rl_spin_3,true,null);
                            Animations.spin(rl_spin_4, false, () ->
                            {
                                ValueAnimator va1 = ValueAnimator.ofFloat(40,0);
                                va1.addUpdateListener(spin_listener);
                                va1.addListener(spin_adapter);
                                va1.start();
                                spin_anim = false;
                            });
                        }
                        super.onAnimationEnd(animation);
                    }
                };
                va.addListener(spin_adapter);
                va.start();
            }
        });




        final RelativeLayout rl_bt_fade = findViewById(R.id.rl_bt_fade_animation);
        final ImageView iv_bt_fade = findViewById(R.id.iv_bt_fade_animation);
        rl_bt_fade.setOnClickListener(view ->
        {
            iv_bt_fade.setVisibility(View.INVISIBLE);
            rl_bt_fade.setClickable(false);
            Animations.fadeOutScaleUp(rl_bt_fade, 1000, 1, 3, () ->
            {
                Handler h = new Handler(Looper.getMainLooper());
                h.postDelayed(() -> Animations.fadeInScaleDown(rl_bt_fade, 1000, 3, 1, () ->
                {
                    iv_bt_fade.setVisibility(View.VISIBLE);
                    rl_bt_fade.setClickable(true);
                }),1000);
            });
        });




        final RelativeLayout rl_bt_shape = findViewById(R.id.rl_bt_shape_animation);
        final ImageView iv_bt_shape = findViewById(R.id.iv_bt_shape_animation);
        final GradientDrawable shape_background = new GradientDrawable();
        Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(() -> shape_background.setCornerRadius(rl_bt_shape.getWidth()/2f),100);
        shape_background.setColor(getColorStateList(R.color.purple_to_dark_gray));
        rl_bt_shape.setBackground(shape_background);
        rl_bt_shape.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                rl_bt_shape.setClickable(false);
                iv_bt_shape.setVisibility(View.INVISIBLE);
                if (b_shape)
                {
                    double[] startValues = new double[]{rl_bt_shape.getWidth()/10f, rl_bt_shape.getWidth()};
                    double[] endValues = new double[]{rl_bt_shape.getWidth()/4f, rl_bt_shape.getWidth()/2f};
                    ValueAnimator va = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
                    va.setDuration(1000);
                    va.addUpdateListener(a1 ->
                    {
                        double[] animatedValue = (double[]) a1.getAnimatedValue();
                        shape_background.setCornerRadius((float)animatedValue[0]);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_bt_shape.getLayoutParams();
                        params.width = (int)animatedValue[1];
                        rl_bt_shape.setLayoutParams(params);
                    });
                    va.addListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            super.onAnimationEnd(animation);
                            rl_bt_shape.setClickable(true);
                            iv_bt_shape.setVisibility(View.VISIBLE);
                            b_shape = false;
                        }
                    });
                    va.start();
                }
                else
                {
                    double[] startValues = new double[]{rl_bt_shape.getWidth()/2f, rl_bt_shape.getWidth()};
                    double[] endValues = new double[]{rl_bt_shape.getWidth()/5f, rl_bt_shape.getWidth()*2};
                    ValueAnimator va = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
                    va.setDuration(1000);
                    va.addUpdateListener(a ->
                    {
                        double[] animatedValue = (double[]) a.getAnimatedValue();
                        shape_background.setCornerRadius((float)animatedValue[0]);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_bt_shape.getLayoutParams();
                        params.width = (int)animatedValue[1];
                        rl_bt_shape.setLayoutParams(params);
                    });
                    va.addListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            super.onAnimationEnd(animation);
                            rl_bt_shape.setClickable(true);
                            iv_bt_shape.setVisibility(View.VISIBLE);
                            b_shape = true;
                        }
                    });
                    va.start();
                }
            }
        });
        b_shape = false;




        final RelativeLayout rl_bt_pie = findViewById(R.id.rl_bt_pie_animation);
        final ImageView iv_bt_pie = findViewById(R.id.iv_bt_pie_animation);
        final CircularProgressIndicator cpi_pie_1 = findViewById(R.id.cpi_pie_animation_1);
        final CircularProgressIndicator cpi_pie_2 = findViewById(R.id.cpi_pie_animation_2);
        rl_bt_pie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cpi_pie_1.setProgress(50);
                cpi_pie_2.setProgress(50);
                iv_bt_pie.setVisibility(View.INVISIBLE);
                rl_bt_pie.setVisibility(View.INVISIBLE);
                ValueAnimator va = ValueAnimator.ofInt(50,0);
                va.setInterpolator(new LinearInterpolator());
                va.setDuration(1200);
                va.addUpdateListener(a ->
                {
                    int animatedValue = (Integer) a.getAnimatedValue();
                    cpi_pie_1.setProgress(animatedValue);
                    cpi_pie_2.setProgress(animatedValue);
                });
                va.addListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        cpi_pie_1.setProgress(0);
                        cpi_pie_2.setProgress(0);
                        Handler h = new Handler(Looper.getMainLooper());
                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ValueAnimator va = ValueAnimator.ofInt(0,50);
                                va.setInterpolator(new LinearInterpolator());
                                va.setDuration(1200);
                                va.addUpdateListener(a ->
                                {
                                    int animatedValue = (Integer) a.getAnimatedValue();
                                    cpi_pie_1.setProgress(animatedValue);
                                    cpi_pie_2.setProgress(animatedValue);
                                });
                                va.addListener(new AnimatorListenerAdapter()
                                {
                                    @Override
                                    public void onAnimationEnd(Animator animation)
                                    {
                                        super.onAnimationEnd(animation);
                                        rl_bt_pie.setVisibility(View.VISIBLE);
                                        iv_bt_pie.setVisibility(View.VISIBLE);
                                    }
                                });
                                va.start();
                            }
                        },600);
                    }
                });
                va.start();
            }
        });




        final RelativeLayout rl_bt_color = findViewById(R.id.rl_bt_color_animation);
        final ImageView iv_bt_color = findViewById(R.id.iv_bt_color_animation);
        final ImageView iv_color_1 = findViewById(R.id.iv_color_animation_1);
        final ImageView iv_color_2 = findViewById(R.id.iv_color_animation_2);
        rl_bt_color.setOnClickListener(view ->
        {
            rl_bt_color.setClickable(false);
            iv_bt_color.setVisibility(View.INVISIBLE);
            iv_color_1.setImageResource(R.drawable.bg_round_button_2);
            iv_color_2.setImageResource(R.drawable.bg_round_button_1);
            Animations.fadeOut(rl_bt_color, 500, () -> Animations.fadeOut(iv_color_2, 500, () ->
            {
                iv_color_2.setImageResource(R.drawable.bg_round_button_3);
                Animations.fadeIn(iv_color_2,500, () ->
                {
                    iv_color_1.setImageResource(R.drawable.bg_round_button_4);
                    Animations.fadeOut(iv_color_2, 500, () ->
                    {
                        iv_color_2.setImageResource(R.drawable.bg_round_button_5);
                        Animations.fadeIn(iv_color_2,500, () -> Animations.fadeIn(rl_bt_color, 500, () ->
                        {
                            rl_bt_color.setClickable(true);
                            iv_bt_color.setVisibility(View.VISIBLE);
                        }));
                    });
                });
            }));
        });
    }
}
