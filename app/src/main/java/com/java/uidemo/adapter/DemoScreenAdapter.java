package com.java.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.MenuActivity;
import com.java.uidemo.R;
import com.java.uidemo.model.DemoScreen;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} for demo screens in {@link MenuActivity}
 */
public class DemoScreenAdapter extends RecyclerView.Adapter
{

    private final Context context;
    private final ArrayList<DemoScreen> demo_screens;

    public DemoScreenAdapter(ArrayList<DemoScreen> demo_screens, Context context)
    {
        this.demo_screens = demo_screens;
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DemoScreenHolder(inflater.inflate(R.layout.item_demo_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        DemoScreenHolder h = (DemoScreenHolder) holder;
        DemoScreen ds = demo_screens.get(position);
        h.iv_bt_item_demo_screen.setImageResource(ds.getIcon_resource());
        if (ds.isEnabled())
        {
            h.rl_bt_item_demo_screen.setOnClickListener(view ->
            {
                ((MenuActivity)context).launchDemoScreen(ds);
            });
            if (ds.getColor_resource() == R.color.ui_demo_red)
            {
                h.rl_bt_item_demo_screen.setBackgroundResource(R.drawable.bg_rounded_button_5);
            }
            else if (ds.getColor_resource() == R.color.ui_demo_purple)
            {
                h.rl_bt_item_demo_screen.setBackgroundResource(R.drawable.bg_rounded_button_4);
            }
            else if (ds.getColor_resource() == R.color.ui_demo_yellow)
            {
                h.rl_bt_item_demo_screen.setBackgroundResource(R.drawable.bg_rounded_button_3);
            }
            else if (ds.getColor_resource() == R.color.ui_demo_blue)
            {
                h.rl_bt_item_demo_screen.setBackgroundResource(R.drawable.bg_rounded_button_2);
            }
            else
            {
                h.rl_bt_item_demo_screen.setBackgroundResource(R.drawable.bg_rounded_button_1);
            }
        }
        else
        {
            h.rl_bt_item_demo_screen.setBackgroundResource(R.drawable.bg_rounded_button_disabled);
        }
    }
    @Override
    public int getItemCount() {
        return demo_screens.size();
    }


    private static class DemoScreenHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_bt_item_demo_screen;
        ImageView iv_bt_item_demo_screen;
        DemoScreenHolder(View v)
        {
            super(v);
            rl_bt_item_demo_screen = v.findViewById(R.id.rl_bt_item_demo_screen);
            iv_bt_item_demo_screen = v.findViewById(R.id.iv_item_demo_screen);
        }
    }
}
