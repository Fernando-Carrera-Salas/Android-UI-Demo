package com.java.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.model.DropdownItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} for {@link com.java.uidemo.view.DropdownButton}
 */
public class DropdownAdapter extends RecyclerView.Adapter
{
    private final ArrayList<DropdownItem> dropdown_items;
    private final Context context;
    private final UIDemo demo;
    private final DropdownListener listener;

    public DropdownAdapter(ArrayList<DropdownItem> dropdown_items, Context context, DropdownListener listener)
    {
        this.dropdown_items = dropdown_items;
        this.context = context;
        this.demo = (UIDemo) context.getApplicationContext();
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new DropdownHolder(inflater.inflate(R.layout.item_dropdown, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final DropdownHolder h = (DropdownHolder) holder;
        final DropdownItem di = dropdown_items.get(position);
        if (position==0)
            h.v_divider_item.setVisibility(View.GONE);
        else
            h.v_divider_item.setVisibility(View.VISIBLE);
        if (di.getIcon_resource()<=0)
            h.iv_item.setVisibility(View.GONE);
        else
        {
            h.iv_item.setVisibility(View.VISIBLE);
            h.iv_item.setImageResource(di.getIcon_resource());
        }
        h.tv_item.setTypeface(demo.getTf_monserrat_light());
        h.tv_item.setText(di.getText());
        h.rl_item.setOnClickListener(view ->
        {
            if (listener!=null)
                listener.onItemSelected(di);
        });
    }

    @Override
    public int getItemCount()
    {
        return dropdown_items.size();
    }


    private static class DropdownHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_item;
        TextView tv_item;
        ImageView iv_item;
        View v_divider_item;
        DropdownHolder(View v)
        {
            super(v);
            rl_item = v.findViewById(R.id.rl_item_dropdown);
            tv_item = v.findViewById(R.id.tv_item_dropdown);
            iv_item = v.findViewById(R.id.iv_item_dropdown);
            v_divider_item = v.findViewById(R.id.v_divider_item_dropdown);
        }
    }

    public interface DropdownListener
    {
        void onItemSelected(DropdownItem item);
    }
}
