package com.java.uidemo.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.demos.fragment.FragmentHome;
import com.java.uidemo.demos.fragment.FragmentNews;
import com.java.uidemo.demos.fragment.FragmentNotifications;
import com.java.uidemo.demos.fragment.FragmentProfile;

/**
 * {@link FragmentStateAdapter} for the ViewPager of {@link ViewPagerActivity}
 */
public class ScreenSlidePagerAdapter extends FragmentStateAdapter
{
    private FragmentHome home;
    private FragmentNews news;
    private FragmentNotifications notifications;
    private FragmentProfile profile;

    public ScreenSlidePagerAdapter(AppCompatActivity fa)
    {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 0:
                home = new FragmentHome();
                return home;
            case 1:
                news = new FragmentNews();
                return news;
            case 2:
                notifications = new FragmentNotifications();
                return notifications;
            case 3:
                profile = new FragmentProfile();
                return profile;
        }
        return home;
    }

    @Override
    public int getItemCount()
    {
        return 4;
    }

    public FragmentHome getHome()
    {
        return home;
    }

    public FragmentNews getNews()
    {
        return news;
    }

    public FragmentNotifications getNotifications()
    {
        return notifications;
    }

    public FragmentProfile getProfile()
    {
        return profile;
    }
}
