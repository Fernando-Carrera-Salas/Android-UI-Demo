package com.java.uidemo.demos.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.uidemo.R;
import com.java.uidemo.adapter.FriendAdapter;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.demos.ViewPagerActivity;
import com.java.uidemo.util.Animations;
import com.java.uidemo.util.Util;

import java.util.ArrayList;
import java.util.Random;

public class FragmentProfile extends Fragment
{
    private Activity activity;
    private UIDemo demo;
    private boolean liked;
    private RelativeLayout rl_friends;
    private RecyclerView rv_friends;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        activity = getActivity();
        demo = (UIDemo) activity.getApplicationContext();
        ScrollView sv_profile = v.findViewById(R.id.sv_profile);
        RelativeLayout rl_fixed_picture = v.findViewById(R.id.rl_fixed_picture_profile);
        sv_profile.setOnScrollChangeListener((view, i, i1, i2, i3) ->
        {
            if (Util.convertPixelsToDp(i1,demo)>200)
            {
                rl_fixed_picture.setVisibility(View.VISIBLE);
            }
            else
            {
                rl_fixed_picture.setVisibility(View.INVISIBLE);
            }
        });
        ImageView iv_fixed_picture = v.findViewById(R.id.iv_fixed_picture_profile);
        iv_fixed_picture.setOnClickListener(view -> ((ViewPagerActivity)activity).displayFullscreenImage(R.drawable.profile_0));
        ImageView iv_scroll_picture = v.findViewById(R.id.iv_scroll_picture_profile);
        iv_scroll_picture.setOnClickListener(view -> ((ViewPagerActivity)activity).displayFullscreenImage(R.drawable.profile_0));
        final ImageView iv_bt_fixed_likes = v.findViewById(R.id.iv_bt_fixed_likes_profile);
        final ImageView iv_bt_scroll_likes = v.findViewById(R.id.iv_bt_scroll_likes_profile);
        RelativeLayout rl_bt_fixed_likes = v.findViewById(R.id.rl_bt_fixed_likes_profile);
        RelativeLayout rl_bt_fixed_friends = v.findViewById(R.id.rl_bt_fixed_friends_profile);
        final int likes = new Random().nextInt(90)+10;
        final int friends = new Random().nextInt(20)+5;
        ArrayList<Pair<Integer, String>> friend_list = new ArrayList<>();
        for (int i=0; i<friends; i++)
        {
            int rand = new Random().nextInt(6);
            int image = 0;
            String name = "";
            switch (rand)
            {
                case 0:
                    image = R.drawable.profile_1;
                    break;
                case 1:
                    image = R.drawable.profile_2;
                    break;
                case 2:
                    image = R.drawable.profile_3;
                    break;
                case 3:
                    image = R.drawable.profile_4;
                    break;
                case 4:
                    image = R.drawable.profile_5;
                    break;
                case 5:
                    image = R.drawable.profile_6;
                    break;
            }
            String[] m_names = getResources().getStringArray(R.array.m_names);
            String[] f_names = getResources().getStringArray(R.array.f_names);
            if (rand%2==0)
                name = m_names[new Random().nextInt(6)];
            else
                name = f_names[new Random().nextInt(6)];
            friend_list.add(new Pair<>(image,name));
        }
        rv_friends = v.findViewById(R.id.rv_friends_profile);
        rv_friends.setLayoutManager(new LinearLayoutManager(activity));
        rv_friends.setAdapter(new FriendAdapter(friend_list,activity));

        rl_friends = v.findViewById(R.id.rl_friends_profile);
        rl_friends.setVisibility(View.INVISIBLE);
        rl_friends.setOnClickListener(view ->
        {
            rv_friends.clearAnimation();
            rl_friends.clearAnimation();
            rl_friends.setVisibility(View.INVISIBLE);
        });

        TextView tv_bt_fixed_likes = v.findViewById(R.id.tv_bt_fixed_likes_profile);
        tv_bt_fixed_likes.setText(String.valueOf(likes));
        tv_bt_fixed_likes.setTypeface(demo.getTf_monserrat_light());
        TextView tv_bt_fixed_friends = v.findViewById(R.id.tv_bt_fixed_friends_profile);
        tv_bt_fixed_friends.setText(String.valueOf(friends));
        tv_bt_fixed_friends.setTypeface(demo.getTf_monserrat_light());
        TextView tv_bt_scroll_likes = v.findViewById(R.id.tv_bt_scroll_likes_profile);
        tv_bt_scroll_likes.setText(String.valueOf(likes));
        tv_bt_scroll_likes.setTypeface(demo.getTf_monserrat_light());
        TextView tv_bt_scroll_friends = v.findViewById(R.id.tv_bt_scroll_friends_profile);
        tv_bt_scroll_friends.setText(String.valueOf(friends));
        tv_bt_scroll_friends.setTypeface(demo.getTf_monserrat_light());
        rl_bt_fixed_likes.setOnClickListener(view ->
        {
            if (!liked)
            {
                liked = true;
                iv_bt_fixed_likes.setImageTintList(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_fixed_likes.setTextColor(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_fixed_likes.setText(String.valueOf(Integer.parseInt(tv_bt_fixed_likes.getText().toString())+1));
                iv_bt_scroll_likes.setImageTintList(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_scroll_likes.setTextColor(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_scroll_likes.setText(String.valueOf(Integer.parseInt(tv_bt_scroll_likes.getText().toString())+1));
            }
        });
        RelativeLayout rl_bt_scroll_likes = v.findViewById(R.id.rl_bt_scroll_likes_profile);
        rl_bt_scroll_likes.setOnClickListener(view ->
        {
            if (!liked)
            {
                liked = true;
                iv_bt_fixed_likes.setImageTintList(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_fixed_likes.setTextColor(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_fixed_likes.setText(String.valueOf(Integer.parseInt(tv_bt_fixed_likes.getText().toString())+1));
                iv_bt_scroll_likes.setImageTintList(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_scroll_likes.setTextColor(activity.getColorStateList(R.color.ui_demo_green));
                tv_bt_scroll_likes.setText(String.valueOf(Integer.parseInt(tv_bt_scroll_likes.getText().toString())+1));
            }
        });
        rl_bt_fixed_friends.setOnClickListener(view -> displayFriends());
        RelativeLayout rl_bt_scroll_friends = v.findViewById(R.id.rl_bt_scroll_friends_profile);
        rl_bt_scroll_friends.setOnClickListener(view -> displayFriends());

        final TextView tv_text_profile = v.findViewById(R.id.tv_text_profile);
        SpannableStringBuilder ssb_paragraph_initial = new SpannableStringBuilder();
        String[] paragraphs = getString(R.string.placeholder_profile).split("\n\n");
        int previous_length = 0;
        int[] caps = new int[paragraphs.length];
        for (int i=0; i<paragraphs.length; i++)
        {
            String s_paragraph = paragraphs[i];
            ssb_paragraph_initial.append(s_paragraph);
            ssb_paragraph_initial.append("\n\n");
            caps[i] = previous_length;
            previous_length = ssb_paragraph_initial.length()+1;
        }
        for (int i=0; i<caps.length; i++)
        {
            ssb_paragraph_initial.setSpan(new RelativeSizeSpan(1.5f),caps[i],caps[i]+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv_text_profile.setText(ssb_paragraph_initial);
        tv_text_profile.setTypeface(demo.getTf_monserrat_light());

        return v;
    }

    private void displayFriends()
    {
        Animations.fadeIn(rl_friends,300,null);
        Animations.slideRightIn(rv_friends,600);
    }

    public boolean checkBackFriends()
    {
        if (rl_friends.getVisibility()==View.VISIBLE)
        {
            rl_friends.performClick();
            return true;
        }
        return false;
    }
}
