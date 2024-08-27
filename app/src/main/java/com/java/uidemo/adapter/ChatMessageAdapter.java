package com.java.uidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.java.uidemo.R;
import com.java.uidemo.application.UIDemo;
import com.java.uidemo.demos.ChatActivity;
import com.java.uidemo.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} for chat messages in {@link ChatActivity}
 */
public class ChatMessageAdapter extends RecyclerView.Adapter
{
    private final ArrayList<ChatMessage> chat_messages;
    private final Context context;
    private final UIDemo demo;
    private final SimpleDateFormat sdf_hhmm = new SimpleDateFormat("HH:mm", Locale.US);

    public ChatMessageAdapter(ArrayList<ChatMessage> chat_messages, Context context)
    {
        this.chat_messages = chat_messages;
        this.context = context;
        this.demo = (UIDemo) context.getApplicationContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ChatMessageHolder(inflater.inflate(R.layout.item_chat_message, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final ChatMessageHolder h = (ChatMessageHolder) holder;
        final ChatMessage cm = chat_messages.get(position);
        RelativeLayout.LayoutParams params;
        if (cm.getUser_id()==0)
        {
            if (cm.getImage_uri()==null)
            {
                h.tv_my_message.setText(cm.getMessage());
                h.tv_my_message.setVisibility(View.VISIBLE);
                h.cv_my_message.setVisibility(View.GONE);
            }
            else
            {
                Glide.with(context).load(cm.getImage_uri()).into(h.iv_my_message);
                h.tv_my_message.setVisibility(View.GONE);
                h.cv_my_message.setVisibility(View.VISIBLE);
                h.iv_my_message.setOnClickListener(view -> ((ChatActivity)context).displayFullscreenImage(cm.getImage_uri()));
            }
            h.tv_date_my_message.setText(sdf_hhmm.format(cm.getDate()));
            h.ll_their_message.setVisibility(View.GONE);
            h.ll_my_message.setVisibility(View.VISIBLE);
            params = (RelativeLayout.LayoutParams) h.ll_my_message.getLayoutParams();
            if (chat_messages.size()>(position+1)&&chat_messages.get(position+1).getUser_id()==0)
            {
                if (position>0&&chat_messages.get(position-1).getUser_id()==0)
                {
                    h.ll_my_message.setBackgroundResource(R.drawable.bg_message_out_multi_1);
                    params.setMargins(params.leftMargin,10,params.rightMargin,params.bottomMargin);
                    h.ll_my_message.setLayoutParams(params);
                }
                else
                    h.ll_my_message.setBackgroundResource(R.drawable.bg_message_out_multi_0);
            }
            else
            {
                if (position>0&&chat_messages.get(position-1).getUser_id()==0)
                {
                    h.ll_my_message.setBackgroundResource(R.drawable.bg_message_out_multi_2);
                    params.setMargins(params.leftMargin,10,params.rightMargin,params.bottomMargin);
                    h.ll_my_message.setLayoutParams(params);
                }
                else
                    h.ll_my_message.setBackgroundResource(R.drawable.bg_message_out_single);
            }
        }
        else
        {
            if (cm.getImage_uri()==null)
            {
                h.tv_their_message.setText(cm.getMessage());
                h.tv_their_message.setVisibility(View.VISIBLE);
                h.cv_their_message.setVisibility(View.GONE);
            }
            else
            {
                Glide.with(context).load(cm.getImage_uri()).into(h.iv_their_message);
                h.tv_their_message.setVisibility(View.GONE);
                h.cv_their_message.setVisibility(View.VISIBLE);
                h.iv_their_message.setOnClickListener(view -> ((ChatActivity)context).displayFullscreenImage(cm.getImage_uri()));
            }
            h.tv_date_their_message.setText(sdf_hhmm.format(cm.getDate()));
            h.ll_their_message.setVisibility(View.VISIBLE);
            h.ll_my_message.setVisibility(View.GONE);
            params = (RelativeLayout.LayoutParams) h.ll_their_message.getLayoutParams();
            if (chat_messages.size()>(position+1)&&chat_messages.get(position+1).getUser_id()==100)
            {
                if (position>0&&chat_messages.get(position-1).getUser_id()==100)
                {
                    h.ll_their_message.setBackgroundResource(R.drawable.bg_message_in_multi_1);
                    params.setMargins(params.leftMargin,10,params.rightMargin,params.bottomMargin);
                    h.ll_their_message.setLayoutParams(params);
                }
                else
                    h.ll_their_message.setBackgroundResource(R.drawable.bg_message_in_multi_0);
            }
            else
            {
                if (position>0&&chat_messages.get(position-1).getUser_id()==100)
                {
                    h.ll_their_message.setBackgroundResource(R.drawable.bg_message_in_multi_2);
                    params.setMargins(params.leftMargin,10,params.rightMargin,params.bottomMargin);
                    h.ll_their_message.setLayoutParams(params);
                }
                else
                    h.ll_their_message.setBackgroundResource(R.drawable.bg_message_in_single);
            }
        }
        h.tv_their_message.setTypeface(demo.getTf_monserrat_light());
        h.tv_my_message.setTypeface(demo.getTf_monserrat_light());
        h.tv_date_my_message.setTypeface(demo.getTf_monserrat_light());
        h.tv_date_their_message.setTypeface(demo.getTf_monserrat_light());
    }

    @Override
    public int getItemCount()
    {
        return chat_messages.size();
    }


    private static class ChatMessageHolder extends RecyclerView.ViewHolder
    {
        TextView tv_their_message, tv_date_their_message;
        TextView tv_my_message, tv_date_my_message;
        ImageView iv_their_message, iv_my_message;
        LinearLayout ll_my_message, ll_their_message;
        CardView cv_their_message, cv_my_message;
        ChatMessageHolder(View v)
        {
            super(v);
            tv_their_message = v.findViewById(R.id.tv_their_message_item_chat_message);
            tv_date_their_message = v.findViewById(R.id.tv_date_their_message_item_chat_message);
            tv_my_message = v.findViewById(R.id.tv_my_message_item_chat_message);
            tv_date_my_message = v.findViewById(R.id.tv_date_my_message_item_chat_message);
            ll_their_message = v.findViewById(R.id.ll_their_message_item_chat_message);
            ll_my_message = v.findViewById(R.id.ll_my_message_item_chat_message);
            iv_their_message = v.findViewById(R.id.iv_their_message_item_chat_message);
            iv_my_message = v.findViewById(R.id.iv_my_message_item_chat_message);
            cv_their_message = v.findViewById(R.id.cv_their_message_item_chat_message);
            cv_my_message = v.findViewById(R.id.cv_my_message_item_chat_message);
        }
    }
}
