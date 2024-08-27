package com.java.uidemo.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.java.uidemo.view.DropdownButton;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.java.uidemo.MenuActivity;
import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.model.DemoScreen;

/**
 * This Activity serves as a base for each {@link DemoScreen}.
 * </p>
 * The common elements, such as the top bar, the {@link PhotoView} or the help text are initialized here.
 */
public class DemoActivity extends AppCompatActivity
{
    private TextView tv_bt_header_help;
    private RelativeLayout rl_help;
    private TextView tv_help;
    protected RelativeLayout rl_root, rl_fullscreen_image;
    private PhotoView pv_fullscreen_image;
    private DemoScreen demo_screen;
    protected UIDemo demo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        demo = (UIDemo) getApplicationContext();
        demo_screen = demo.getDemo_screen();
        setDefaultBack();
    }
    protected void initDemo()
    {
        rl_root = findViewById(R.id.rl_root);

        LayoutInflater inflater = LayoutInflater.from(this);


        rl_help = (RelativeLayout) inflater.inflate(R.layout.demo_help, rl_root, false);
        rl_root.addView(rl_help);
        rl_help.setVisibility(View.INVISIBLE);
        tv_help = rl_help.findViewById(R.id.tv_help);
        tv_help.setTypeface(demo.getTf_monserrat_light());

        RelativeLayout rl_header = (RelativeLayout) inflater.inflate(R.layout.demo_header, rl_root, false);
        rl_root.addView(rl_header);
        ImageView iv_bt_back = rl_header.findViewById(R.id.iv_bt_back_header);
        iv_bt_back.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        TextView tv_header = rl_header.findViewById(R.id.tv_header);
        tv_header.setTypeface(demo.getTf_ashley_semibold());
        tv_bt_header_help = rl_header.findViewById(R.id.tv_bt_help_header);
        tv_bt_header_help.setTypeface(demo.getTf_ashley_semibold());
        if (demo_screen!=null)
        {
            rl_header.setBackgroundColor(getColor(demo_screen.getColor_resource()));
            tv_header.setText(getString(demo_screen.getName_resource()));
            tv_bt_header_help.setOnClickListener(view -> display_help(getString(demo_screen.getHelp_resource())));
        }
        rl_fullscreen_image = (RelativeLayout) inflater.inflate(R.layout.demo_fullscreen_image, rl_root, false);
        rl_root.addView(rl_fullscreen_image);
        pv_fullscreen_image = rl_fullscreen_image.findViewById(R.id.pv_fullscreen_image);
        pv_fullscreen_image.setOnClickListener(view -> rl_fullscreen_image.setVisibility(View.GONE));
        rl_fullscreen_image.setOnClickListener(view -> rl_fullscreen_image.setVisibility(View.GONE));

    }

    protected void setDefaultBack()
    {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (!checkBack())
                {
                    menu();
                }
            }
        });
    }

    protected boolean checkBack()
    {
        if (rl_help!=null&&rl_help.getVisibility()==View.VISIBLE)
        {
            rl_help.setVisibility(View.INVISIBLE);
            tv_bt_header_help.setVisibility(View.VISIBLE);
            return true;
        }
        else
        {
            if (rl_fullscreen_image!=null&&rl_fullscreen_image.getVisibility()==View.VISIBLE)
            {
                rl_fullscreen_image.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }

    protected void menu()
    {
        Intent i = new Intent(DemoActivity.this, MenuActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        finish();
    }

    public void displayFullscreenImage(int image_resource)
    {
        Glide.with(DemoActivity.this).load(image_resource).into(pv_fullscreen_image);
        rl_fullscreen_image.setVisibility(View.VISIBLE);
    }

    public void displayFullscreenImage(Uri image_uri)
    {
        if (image_uri!=null)
        {
            Glide.with(DemoActivity.this).load(image_uri).into(pv_fullscreen_image);
            rl_fullscreen_image.setVisibility(View.VISIBLE);
        }
    }

    public void displayFullscreenImage(Bitmap image_bitmap)
    {
        if (image_bitmap!=null)
        {
            Glide.with(DemoActivity.this).load(image_bitmap).into(pv_fullscreen_image);
            rl_fullscreen_image.setVisibility(View.VISIBLE);
        }
    }

    public void displayFullscreenImage(String image_url)
    {
        if (image_url!=null&&!image_url.isEmpty())
        {
            Glide.with(DemoActivity.this).load(image_url).into(pv_fullscreen_image);
            rl_fullscreen_image.setVisibility(View.VISIBLE);
        }
    }

    public void display_help(String help_text)
    {
        Animations.slideDownIn(rl_help,600);
        tv_help.setText(Html.fromHtml(help_text, Html.FROM_HTML_MODE_COMPACT));
        tv_bt_header_help.setVisibility(View.INVISIBLE);
    }

    private GlobalTouchListener global_touch_listener;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (global_touch_listener != null)
            {
                global_touch_listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * This interface informs custom views, such as the {@link DropdownButton}, when the user has touched anywhere on the screen.
     */
    public interface GlobalTouchListener
    {
        void onTouch(MotionEvent ev);
    }

    public void setGlobal_touch_listener(GlobalTouchListener global_touch_listener)
    {
        this.global_touch_listener = global_touch_listener;
    }

    public void clearAllFocus()
    {
        if (rl_root!=null)
            rl_root.clearFocus();
    }
}
