package com.java.uidemo.demos;

import static com.java.uidemo.util.Constants.REQUEST_NOTIFICATIONS;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.java.uidemo.R;
import com.java.uidemo.SplashActivity;
import com.java.uidemo.adapter.ScreenSlidePagerAdapter;
import com.java.uidemo.model.DemoNotification;
import com.java.uidemo.model.NewsArticle;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

/**
 * This {@link DemoActivity} displays a {@link ViewPager2} with four pages.
 * </p>
 * The pages are added through the {@link ScreenSlidePagerAdapter}.
 */
public class ViewPagerActivity extends DemoActivity
{
    private RelativeLayout rl_bt_home, rl_bt_news, rl_bt_notifications, rl_bt_profile;
    private ImageView iv_bt_home, iv_bt_news, iv_bt_notifications, iv_bt_profile;
    private ViewPager2 vp;
    private ScreenSlidePagerAdapter screen_slide_pager_adapter;

    private DemoNotification new_notification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initDemo();
        rl_bt_home = findViewById(R.id.rl_bt_home_viewpager);
        rl_bt_news = findViewById(R.id.rl_bt_news_viewpager);
        rl_bt_notifications = findViewById(R.id.rl_bt_notifications_viewpager);
        rl_bt_profile = findViewById(R.id.rl_bt_profile_viewpager);
        iv_bt_home = findViewById(R.id.iv_bt_home_viewpager);
        iv_bt_news = findViewById(R.id.iv_bt_news_viewpager);
        iv_bt_notifications = findViewById(R.id.iv_bt_notifications_viewpager);
        iv_bt_profile = findViewById(R.id.iv_bt_profile_viewpager);

        vp = findViewById(R.id.vp_viewpager);
        screen_slide_pager_adapter = new ScreenSlidePagerAdapter(this);
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                switch_tab(position);
                if (screen_slide_pager_adapter.getNotifications()!=null&&screen_slide_pager_adapter.getNotifications().isAdded())
                    screen_slide_pager_adapter.getNotifications().resetProgress();
                super.onPageSelected(position);
            }
        });
        vp.setAdapter(screen_slide_pager_adapter);
        rl_bt_home.setOnClickListener(view ->
        {
            switch_tab(0);
            vp.setCurrentItem(0);
        });
        rl_bt_news.setOnClickListener(view ->
        {
            switch_tab(1);
            vp.setCurrentItem(1);
        });
        rl_bt_notifications.setOnClickListener(view ->
        {
            switch_tab(2);
            vp.setCurrentItem(2);
        });
        rl_bt_profile.setOnClickListener(view ->
        {
            switch_tab(3);
            vp.setCurrentItem(3);
        });

        switch_tab(0);
        vp.setCurrentItem(0);
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                if (!checkBack())
                {
                    switch(vp.getCurrentItem())
                    {
                        case 1:
                            if (screen_slide_pager_adapter.getNews()!=null&&screen_slide_pager_adapter.getNews().isAdded()&&!screen_slide_pager_adapter.getNews().checkBackFullArticle())
                            {
                                menu();
                            }
                            break;
                        case 3:
                            if (screen_slide_pager_adapter.getProfile()!=null&&screen_slide_pager_adapter.getProfile().isAdded()&&!screen_slide_pager_adapter.getProfile().checkBackFriends())
                            {
                                menu();
                            }
                            break;
                        case 2:
                        case 0:
                            menu();
                            break;
                    }
                }
            }
        });
    }
    private void switch_tab(int position)
    {
        LinearLayout.LayoutParams params_home = (LinearLayout.LayoutParams) rl_bt_home.getLayoutParams();
        LinearLayout.LayoutParams params_news = (LinearLayout.LayoutParams) rl_bt_news.getLayoutParams();
        LinearLayout.LayoutParams params_notifications = (LinearLayout.LayoutParams) rl_bt_notifications.getLayoutParams();
        LinearLayout.LayoutParams params_profile = (LinearLayout.LayoutParams) rl_bt_profile.getLayoutParams();
        switch (position)
        {
            case 0:
                params_home.weight = 1f;
                params_news.weight = 1.2f;
                params_notifications.weight = 1.2f;
                params_profile.weight = 1.2f;
                rl_bt_home.setLayoutParams(params_home);
                rl_bt_news.setLayoutParams(params_news);
                rl_bt_notifications.setLayoutParams(params_notifications);
                rl_bt_profile.setLayoutParams(params_profile);
                iv_bt_home.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.black));
                iv_bt_news.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_notifications.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_profile.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                break;
            case 1:
                params_home.weight = 1.2f;
                params_news.weight = 1f;
                params_notifications.weight = 1.2f;
                params_profile.weight = 1.2f;
                rl_bt_home.setLayoutParams(params_home);
                rl_bt_news.setLayoutParams(params_news);
                rl_bt_notifications.setLayoutParams(params_notifications);
                rl_bt_profile.setLayoutParams(params_profile);
                iv_bt_home.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_news.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.black));
                iv_bt_notifications.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_profile.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                break;
            case 2:
                params_home.weight = 1.2f;
                params_news.weight = 1.2f;
                params_notifications.weight = 1f;
                params_profile.weight = 1.2f;
                rl_bt_home.setLayoutParams(params_home);
                rl_bt_news.setLayoutParams(params_news);
                rl_bt_notifications.setLayoutParams(params_notifications);
                rl_bt_profile.setLayoutParams(params_profile);
                iv_bt_home.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_news.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_notifications.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.black));
                iv_bt_profile.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                break;
            case 3:
                params_home.weight = 1.2f;
                params_news.weight = 1.2f;
                params_notifications.weight = 1.2f;
                params_profile.weight = 1f;
                rl_bt_home.setLayoutParams(params_home);
                rl_bt_news.setLayoutParams(params_news);
                rl_bt_notifications.setLayoutParams(params_notifications);
                rl_bt_profile.setLayoutParams(params_profile);
                iv_bt_home.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_news.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_notifications.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.light_gray));
                iv_bt_profile.setColorFilter(ContextCompat.getColor(ViewPagerActivity.this,R.color.black));
                break;
        }
    }

    public void display_full_article(NewsArticle article, int position)
    {
        if (screen_slide_pager_adapter!=null&&screen_slide_pager_adapter.getNews()!=null&&screen_slide_pager_adapter.getNews().isAdded()&&vp.getCurrentItem()==1)
            screen_slide_pager_adapter.getNews().displayFullArticle(article, position);
    }

    public void sendNotification()
    {
        new_notification = new DemoNotification();
        int new_id = demo.getLastNotificationId()+1;
        String text_id = String.valueOf(new_id);
        if (new_id<1000)
            text_id = "0"+text_id;
        if (new_id<100)
            text_id = "0"+text_id;
        if (new_id<10)
            text_id = "0"+text_id;
        new_notification.setTitle(getString(R.string.notification_title)+" "+text_id);
        new_notification.setContent(Util.randomPieceOfLorem(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATIONS);
        }
        else
        {
            Util.sendNotification(this, SplashActivity.class,new_notification.getTitle(),new_notification.getContent(),R.mipmap.ic_launcher);
            demo.insertNotification(new_notification.getTitle(),new_notification.getContent());
            screen_slide_pager_adapter.getNotifications().notifyNew();
        }
    }

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[] grant_results)
    {
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
        if (grant_results[0] == PackageManager.PERMISSION_GRANTED)
        {
            Util.sendNotification(this, SplashActivity.class,new_notification.getTitle(),new_notification.getContent(),R.mipmap.ic_launcher);
            demo.insertNotification(new_notification.getTitle(),new_notification.getContent());
            screen_slide_pager_adapter.getNotifications().notifyNew();
        }
    }

    public void loadMoreNews()
    {
        screen_slide_pager_adapter.getNews().loadMoreNews();
    }
}
