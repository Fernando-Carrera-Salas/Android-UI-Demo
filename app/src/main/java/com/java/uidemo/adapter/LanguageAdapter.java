package com.java.uidemo.adapter;

import static com.java.uidemo.util.Constants.LANGUAGE_PREFERENCE;
import static com.java.uidemo.util.Constants.SHARED_PREFERENCES;

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
import com.java.uidemo.demos.LanguageActivity;
import com.java.uidemo.model.DemoLanguage;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} for languages in {@link LanguageActivity}
 */
public class LanguageAdapter extends RecyclerView.Adapter
{

    private Context context;
    private ArrayList<DemoLanguage> languages;
    private final UIDemo demo;

    public LanguageAdapter(ArrayList<DemoLanguage> languages, Context context)
    {
        this.languages = languages;
        this.context = context;
        this.demo = (UIDemo) context.getApplicationContext();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new LanguageHolder(inflater.inflate(R.layout.item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        LanguageHolder h = (LanguageHolder) holder;
        DemoLanguage dl = languages.get(position);
        h.iv_bt_item.setImageResource(dl.getIcon_resource());
        if (dl.getIso_code().equals(context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(LANGUAGE_PREFERENCE,context.getString(R.string.default_language))))
            h.iv_bg_bt_item.setImageResource(R.drawable.bg_language_selected);
        else
            h.iv_bg_bt_item.setImageResource(R.drawable.bg_language);
        h.tv_bt_item.setTypeface(demo.getTf_ashley_semibold());
        h.tv_bt_item.setText(context.getString(dl.getName_resource()));
        h.rl_bt_item.setOnClickListener(view -> ((LanguageActivity)context).selectLanguage(dl));
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }


    private static class LanguageHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl_bt_item;
        ImageView iv_bg_bt_item, iv_bt_item;
        TextView tv_bt_item;
        LanguageHolder(View v)
        {
            super(v);
            rl_bt_item = v.findViewById(R.id.rl_bt_item_language);
            iv_bt_item = v.findViewById(R.id.iv_bt_item_language);
            iv_bg_bt_item = v.findViewById(R.id.iv_bg_bt_item_language);
            tv_bt_item = v.findViewById(R.id.tv_bt_item_language);
        }
    }
}
