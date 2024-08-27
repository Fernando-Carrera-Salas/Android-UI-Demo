package com.java.uidemo.demos.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.uidemo.R;
import com.java.uidemo.adapter.NotificationAdapter;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.view.ProgressBarButton;

import java.util.Random;

/**
 * This {@link Fragment} displays a {@link RecyclerView} with notifications.
 * </p><p>
 * A {@link ProgressBarButton} allows the user to launch a new notification, which will appear on his notification tray and be added to the top of the list.
 */
public class FragmentNotifications extends Fragment
{
    UIDemo demo;
    private Activity activity;
    private ProgressBarButton pbb_bt_receive_new;
    private NotificationAdapter adapter;
    private LinearLayoutManager manager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        activity = getActivity();
        demo = (UIDemo) activity.getApplicationContext();
        RecyclerView rv_notifications = v.findViewById(R.id.rv_notifications);
        manager = new LinearLayoutManager(activity);
        rv_notifications.setLayoutManager(manager);
        adapter = new NotificationAdapter(demo.getNotifications(),activity);
        rv_notifications.setAdapter(adapter);
        pbb_bt_receive_new = v.findViewById(R.id.pbb_bt_receive_new_notifications);
        pbb_bt_receive_new.setup(v.findViewById(R.id.pb_bt_receive_new_notifications),v.findViewById(R.id.rl_bt_receive_new_notifications),2000);
        pbb_bt_receive_new.setListener(new ProgressBarButton.ProgressBarButtonEventListener()
        {
            @Override
            public void onProgressUpdate(int progress)
            {

            }

            @Override
            public void onPress()
            {

            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onEnd()
            {
                ((ViewPagerActivity)activity).sendNotification();
            }
        });

        final SwipeRefreshLayout srl_rv_notifications = v.findViewById(R.id.srl_rv_notifications);
        srl_rv_notifications.setOnRefreshListener(() -> {
            Handler h = new Handler(Looper.getMainLooper());
            h.postDelayed(() -> srl_rv_notifications.setRefreshing(false),(new Random().nextInt(2500)+500));
        });
        srl_rv_notifications.setRefreshing(false);
        return v;
    }
    public void resetProgress()
    {
        try
        {
            pbb_bt_receive_new.resetProgress();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void notifyNew()
    {
        adapter.notifyItemInserted(0);
        manager.scrollToPosition(0);
    }
}
