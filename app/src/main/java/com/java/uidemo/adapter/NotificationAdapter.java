package com.java.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.demos.fragment.FragmentNotifications;
import com.java.uidemo.model.DemoNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} for notifications in {@link ViewPagerActivity}/{@link FragmentNotifications}
 */
public class NotificationAdapter extends RecyclerView.Adapter
{
    private final ArrayList<DemoNotification> notifications;
    private final Context context;
    private final UIDemo demo;
    private final SimpleDateFormat sdf;

    public NotificationAdapter(ArrayList<DemoNotification> notifications, Context context)
    {
        this.notifications = notifications;
        this.context = context;
        this.demo = (UIDemo) context.getApplicationContext();
        if (context.getText(R.string.default_language).equals("EN"))
            sdf = new SimpleDateFormat("HH:mm\nMM/dd/yyyy", Locale.getDefault());
        else
            sdf = new SimpleDateFormat("HH:mm\ndd/MM/yyyy", Locale.getDefault());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new NotificationHolder(inflater.inflate(R.layout.item_notification, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final NotificationHolder h = (NotificationHolder) holder;
        final DemoNotification n = notifications.get(position);
        h.tv_title.setText(n.getTitle());
        h.tv_title.setTypeface(demo.getTf_ashley_semibold());
        h.tv_text.setText(n.getContent());
        h.tv_text.setTypeface(demo.getTf_monserrat_light());
        h.tv_date.setText(sdf.format(n.getDate()));
        h.tv_date.setTypeface(demo.getTf_monserrat_light());
    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }


    private static class NotificationHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title, tv_text, tv_date;
        NotificationHolder(View v)
        {
            super(v);
            tv_title = v.findViewById(R.id.tv_title_item_notification);
            tv_text = v.findViewById(R.id.tv_text_item_notification);
            tv_date = v.findViewById(R.id.tv_date_item_notification);
        }
    }
}
