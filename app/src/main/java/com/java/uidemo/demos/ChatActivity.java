package com.java.uidemo.demos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.java.uidemo.R;
import com.java.uidemo.adapter.ChatMessageAdapter;
import com.java.uidemo.model.ChatMessage;
import com.java.uidemo.util.CustomFocusChangeListener;
import com.java.uidemo.util.DemoActivity;
import com.java.uidemo.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * This {@link DemoActivity} displays a chat.
 * </p><p>
 * The user can write and send messages.
 * </p><p>
 * Images can be sent from the gallery or camera. Using <a href="https://github.com/Dhaval2404/ImagePicker">Dhaval2404's ImagePicker library.</a>
 * </p><p>
 * The app will occasionally respond with random pre-established messages.
 * </p><p>
 * The messages are displayed using a simple {@link RecyclerView}, allowing for maximum customization and control.
 */
public class ChatActivity extends DemoActivity
{
    private ArrayList<ChatMessage> chat_messages;
    private ChatMessageAdapter chat_message_adapter;
    private LinearLayoutManager manager_chat_message;

    private final int MIN_RECEIVE_TIME = 5000;
    private final int MAX_RECEIVE_TIME = 60000;
    private final int MIN_RESPONSE_TIME = 1000;
    private final int MAX_RESPONSE_TIME = 20000;
    private boolean is_response;

    private Handler h_receive_message;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initDemo();
        chat_messages = new ArrayList<>();
        final RecyclerView rv_messages = findViewById(R.id.rv_chat_messages_chat);
        chat_message_adapter = new ChatMessageAdapter(chat_messages, ChatActivity.this);
        manager_chat_message = new LinearLayoutManager(ChatActivity.this);
        manager_chat_message.setStackFromEnd(true);
        rv_messages.setLayoutManager(manager_chat_message);
        rv_messages.setAdapter(chat_message_adapter);
        final TextView tv_name_chat = findViewById(R.id.tv_name_chat);
        tv_name_chat.setTypeface(demo.getTf_ashley_semibold());
        final ImageView iv_profile = findViewById(R.id.iv_profile_chat);
        int rand = new Random().nextInt(6);
        switch (rand)
        {
            case 0:
                iv_profile.setOnClickListener(view -> displayFullscreenImage(R.drawable.profile_1));
                Glide.with(ChatActivity.this).load(R.drawable.profile_1).into(iv_profile);
                break;
            case 1:
                iv_profile.setOnClickListener(view -> displayFullscreenImage(R.drawable.profile_2));
                Glide.with(ChatActivity.this).load(R.drawable.profile_2).into(iv_profile);
                break;
            case 2:
                iv_profile.setOnClickListener(view -> displayFullscreenImage(R.drawable.profile_3));
                Glide.with(ChatActivity.this).load(R.drawable.profile_3).into(iv_profile);
                break;
            case 3:
                iv_profile.setOnClickListener(view -> displayFullscreenImage(R.drawable.profile_4));
                Glide.with(ChatActivity.this).load(R.drawable.profile_4).into(iv_profile);
                break;
            case 4:
                iv_profile.setOnClickListener(view -> displayFullscreenImage(R.drawable.profile_5));
                Glide.with(ChatActivity.this).load(R.drawable.profile_5).into(iv_profile);
                break;
            case 5:
                iv_profile.setOnClickListener(view -> displayFullscreenImage(R.drawable.profile_6));
                Glide.with(ChatActivity.this).load(R.drawable.profile_6).into(iv_profile);
                break;
        }
        String[] m_names = getResources().getStringArray(R.array.m_names);
        String[] f_names = getResources().getStringArray(R.array.f_names);
        if (rand%2==0)
            tv_name_chat.setText(m_names[new Random().nextInt(6)]);
        else
            tv_name_chat.setText(f_names[new Random().nextInt(6)]);

        final ImageView iv_bt_send = findViewById(R.id.iv_bt_send_chat);
        final EditText et_chat = findViewById(R.id.et_chat);
        et_chat.setTypeface(demo.getTf_monserrat_light());
        et_chat.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND)
            {
                iv_bt_send.performClick();
                return true;
            }
            return false;
        });
        et_chat.setOnFocusChangeListener(new CustomFocusChangeListener());
        iv_bt_send.setOnClickListener(view -> {
            Util.hideKeyboard(ChatActivity.this);
            clearAllFocus();
            if (!et_chat.getText().toString().trim().isEmpty())
            {
                ChatMessage cm = new ChatMessage();
                cm.setUser_id(0);
                cm.setMessage(et_chat.getText().toString());
                cm.setDate(new Date());
                chat_messages.add(cm);
                chat_message_adapter.notifyItemInserted(chat_messages.size()-1);
                if (chat_messages.size()>1&&chat_messages.get(chat_messages.size()-2).getUser_id()==0)
                    chat_message_adapter.notifyItemChanged(chat_messages.size()-2);
                manager_chat_message.scrollToPosition(chat_messages.size()-1);

                if (new Random().nextBoolean())
                {
                    is_response = true;
                    h_receive_message.removeCallbacks(r_receive_message);
                    h_receive_message.postDelayed(r_receive_message, new Random().nextInt(MAX_RESPONSE_TIME-MIN_RESPONSE_TIME)+MIN_RESPONSE_TIME);
                }
            }
            et_chat.setText("");
        });
        final ImageView iv_bt_send_image = findViewById(R.id.iv_bt_send_image_chat);
        iv_bt_send_image.setOnClickListener(view -> {
            clearAllFocus();
            Util.hideKeyboard(ChatActivity.this);
            ImagePicker.with(ChatActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        h_receive_message = new Handler(Looper.getMainLooper());
        h_receive_message.postDelayed(r_receive_message, new Random().nextInt(MAX_RECEIVE_TIME-MIN_RECEIVE_TIME)+MIN_RECEIVE_TIME);
    }
    private final Runnable r_receive_message = new Runnable()
    {
        @Override
        public void run()
        {
            h_receive_message.removeCallbacks(r_receive_message);

            ChatMessage cm = new ChatMessage();
            cm.setUser_id(100);
            cm.setMessage(random_message());
            cm.setDate(new Date());
            chat_messages.add(cm);
            chat_message_adapter.notifyItemInserted(chat_messages.size()-1);
            if (chat_messages.size()>1&&chat_messages.get(chat_messages.size()-2).getUser_id()==100)
                chat_message_adapter.notifyItemChanged(chat_messages.size()-2);
            manager_chat_message.scrollToPosition(chat_messages.size()-1);

            h_receive_message.postDelayed(r_receive_message, new Random().nextInt(MAX_RECEIVE_TIME-MIN_RECEIVE_TIME)+MIN_RECEIVE_TIME);
        }
    };

    private String random_message()
    {
        String[] chat_messages = getResources().getStringArray(R.array.chat_messages);
        if (is_response)
            chat_messages = getResources().getStringArray(R.array.chat_responses);
        is_response = false;
        return chat_messages[new Random().nextInt(chat_messages.length)];
    }


    @Override
    protected void onActivityResult(int request_code, int result_code, @Nullable Intent data)
    {
        super.onActivityResult(request_code, result_code, data);
        if (result_code == Activity.RESULT_OK)
        {
            if (data!=null)
            {
                Uri uri = data.getData();
                if (uri!=null)
                {
                    try
                    {
                        ChatMessage cm = new ChatMessage();
                        cm.setUser_id(0);
                        cm.setMessage("");
                        cm.setImage_uri(uri);
                        cm.setDate(new Date());
                        chat_messages.add(cm);
                        chat_message_adapter.notifyItemInserted(chat_messages.size()-1);
                        if (chat_messages.size()>1&&chat_messages.get(chat_messages.size()-2).getUser_id()==0)
                            chat_message_adapter.notifyItemChanged(chat_messages.size()-2);
                        manager_chat_message.scrollToPosition(chat_messages.size()-1);

                        if (new Random().nextBoolean())
                        {
                            is_response = true;
                            h_receive_message.removeCallbacks(r_receive_message);
                            h_receive_message.postDelayed(r_receive_message, new Random().nextInt(MAX_RESPONSE_TIME-MIN_RESPONSE_TIME)+MIN_RESPONSE_TIME);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
