package com.java.uidemo.demos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;

/**
 * This {@link Fragment} simply displays the movements used to interact with the ViewPager.
 */
public class FragmentHome extends Fragment
{
    UIDemo demo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        demo = (UIDemo) getContext().getApplicationContext();
        TextView tv_swipe = v.findViewById(R.id.tv_swipe_home);
        tv_swipe.setTypeface(demo.getTf_ashley_semibold());
        TextView tv_press = v.findViewById(R.id.tv_press_home);
        tv_press.setTypeface(demo.getTf_ashley_semibold());
        return v;
    }
}
