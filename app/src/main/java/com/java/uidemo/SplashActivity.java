package com.java.uidemo;

import static com.java.uidemo.util.Constants.LANGUAGE_PREFERENCE;
import static com.java.uidemo.util.Constants.SHARED_PREFERENCES;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.java.uidemo.application.UIDemo;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.Util;

/**
 * This {@link AppCompatActivity} adds a custom splash screen animation.
 * </p><p>
 * The settings from <b>styles-v31.xml</b> skip the default splash screen in Android 12+ devices.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{
    private View v_background, v_click;
    private ImageView iv_logo;
    private TextView tv_title_1, tv_title_2, tv_title_3;
    private LinearLayout ll_splash;
    private View v_logo_splash;
    private int i_splash;
    private UIDemo demo;
    private Handler h_splash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*
         * If a language has been selected previously, it will be stored in the SharedPreferences, and we can retrieve it.
         * If not, the device language will be used. If the device language does not have its own XML file, the default (English) will be used.
         */
        Util.setLocale(SplashActivity.this,getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE).getString(LANGUAGE_PREFERENCE,getString(R.string.default_language)));

        demo = (UIDemo) getApplicationContext();
        v_background = findViewById(R.id.v_background_splash);
        iv_logo = findViewById(R.id.iv_logo_splash);
        tv_title_1 = findViewById(R.id.tv_title_splash_1);
        tv_title_1.setTypeface(demo.getTf_ashley_semibold());
        tv_title_2 = findViewById(R.id.tv_title_splash_2);
        tv_title_2.setTypeface(demo.getTf_ashley_semibold());
        tv_title_3 = findViewById(R.id.tv_title_splash_3);
        tv_title_3.setTypeface(demo.getTf_ashley_semibold());
        ll_splash = findViewById(R.id.ll_splash);
        ll_splash.setVisibility(View.INVISIBLE);
        v_logo_splash = findViewById(R.id.v_logo_splash);

        /*
         * Allow the user to skip the splash animation.
         */
        v_click = findViewById(R.id.v_click_splash);
        v_click.setOnClickListener(view -> {
            if (!demo.isB_splash())
            {
                endSplash();
                v_click.setOnClickListener(null);
            }
        });

        if (demo.isB_splash())
        {
            endSplash();
        }
        else
        {
            i_splash = 0;
            h_splash = new Handler(Looper.getMainLooper());
            h_splash.postDelayed(r_splash,500);
        }
    }

    /**
     * This {@link Runnable} controls each step of the splash animation.
     */
    private final Runnable r_splash = new Runnable()
    {
        @Override
        public void run()
        {
            h_splash.removeCallbacks(r_splash);
            switch (i_splash)
            {
                case 0:
                    v_background.setVisibility(View.INVISIBLE);
                    iv_logo.setVisibility(View.INVISIBLE);
                    v_logo_splash.setVisibility(View.INVISIBLE);
                    ll_splash.setVisibility(View.INVISIBLE);
                    tv_title_1.setVisibility(View.INVISIBLE);
                    tv_title_2.setVisibility(View.INVISIBLE);
                    tv_title_3.setVisibility(View.INVISIBLE);
                    v_background.setBackgroundColor(getColor(R.color.black));
                    iv_logo.setColorFilter(getColor(R.color.ui_demo_green));
                    v_logo_splash.setBackgroundColor(getColor(R.color.ui_demo_green));
                    Animations.fadeIn(v_background,1000,null);
                    i_splash = 1;
                    h_splash.postDelayed(r_splash,1500);
                    break;
                case 1:
                    v_background.setVisibility(View.VISIBLE);
                    iv_logo.setVisibility(View.VISIBLE);
                    v_logo_splash.setVisibility(View.VISIBLE);
                    ll_splash.setVisibility(View.VISIBLE);
                    DisplayMetrics display_metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(display_metrics);
                    int height = display_metrics.heightPixels;
                    Animations.slideUpIn(ll_splash,900);
                    ValueAnimator va = ValueAnimator.ofFloat(height,0);
                    va.setDuration(1200);
                    va.addUpdateListener(valueAnimator ->
                    {
                        float f = ((Float) valueAnimator.getAnimatedValue());
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v_logo_splash.getLayoutParams();
                        params.height = (int) f;
                        v_logo_splash.setLayoutParams(params);
                    });
                    va.start();
                    i_splash = 2;
                    h_splash.postDelayed(r_splash,1500);
                    break;
                case 2:
                    tv_title_1.setVisibility(View.VISIBLE);
                    tv_title_1.setTextColor(getColor(R.color.ui_demo_purple));
                    v_background.setBackgroundColor(getColor(R.color.ui_demo_green));
                    iv_logo.setColorFilter(getColor(R.color.ui_demo_purple));
                    i_splash = 3;
                    h_splash.postDelayed(r_splash,500);
                    break;
                case 3:
                    tv_title_1.setTextColor(getColor(R.color.ui_demo_yellow));
                    tv_title_2.setVisibility(View.VISIBLE);
                    tv_title_2.setTextColor(getColor(R.color.ui_demo_yellow));
                    v_background.setBackgroundColor(getColor(R.color.ui_demo_purple));
                    iv_logo.setColorFilter(getColor(R.color.ui_demo_yellow));
                    i_splash = 4;
                    h_splash.postDelayed(r_splash,500);
                    break;
                case 4:
                    endSplash();
                    break;
                case 5:
                    menu();
                    break;
            }
        }
    };

    private void endSplash()
    {
        i_splash = 5;
        demo.setB_splash(true);
        if (h_splash!=null)
            h_splash.removeCallbacks(r_splash);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v_logo_splash.getLayoutParams();
        params.height = 0;
        v_logo_splash.setLayoutParams(params);
        v_background.setBackgroundColor(getColor(R.color.colorAccent));
        v_background.setVisibility(View.VISIBLE);
        iv_logo.setVisibility(View.VISIBLE);
        iv_logo.setColorFilter(getColor(R.color.ui_demo_green));
        ll_splash.setVisibility(View.VISIBLE);
        v_logo_splash.setVisibility(View.VISIBLE);
        tv_title_1.setTextColor(getColor(R.color.colorPrimaryDark));
        tv_title_1.setVisibility(View.VISIBLE);
        tv_title_2.setTextColor(getColor(R.color.colorPrimaryDark));
        tv_title_2.setVisibility(View.VISIBLE);
        tv_title_3.setTextColor(getColor(R.color.colorPrimaryDark));
        tv_title_3.setVisibility(View.VISIBLE);
        h_splash = new Handler(Looper.getMainLooper());
        h_splash.postDelayed(r_splash, 1000);
    }

    private void menu()
    {
        i_splash = 6;
        demo.setB_splash(true);
        h_splash.removeCallbacks(r_splash);
        Intent i = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        finish();
    }

}
