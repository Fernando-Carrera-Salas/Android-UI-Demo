package com.java.uidemo.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.demos.fragment.FragmentProfile;

import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} for friends in {@link ViewPagerActivity}/{@link FragmentProfile}
 */
public class FriendAdapter extends RecyclerView.Adapter
{
    private final ArrayList<Pair<Integer,String>> friends;
    private final Context context;
    private final UIDemo demo;

    public FriendAdapter(ArrayList<Pair<Integer,String>> friends, Context context)
    {
        this.friends = friends;
        this.context = context;
        this.demo = (UIDemo) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new FriendHolder(inflater.inflate(R.layout.item_friend, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final FriendHolder h = (FriendHolder) holder;
        final Pair<Integer,String> f = friends.get(position);
        h.iv_image.setImageResource(f.first);
        h.tv_name.setText(f.second);
        h.tv_name.setTypeface(demo.getTf_ashley_semibold());
    }

    @Override
    public int getItemCount()
    {
        return friends.size();
    }


    private static class FriendHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_image;
        TextView tv_name;
        FriendHolder(View v)
        {
            super(v);
            tv_name = v.findViewById(R.id.tv_name_item_friend);
            iv_image = v.findViewById(R.id.iv_image_item_friend);
        }
    }
}
