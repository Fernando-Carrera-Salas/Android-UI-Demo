package com.java.uidemo.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class ChatMessage
{
    private int user_id;
    private String message;
    private Date date;
    private Uri image_uri;

    public ChatMessage()
    {
        user_id = -1;
        message = "";
        image_uri = null;
        date = new Date(0);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public Uri getImage_uri()
    {
        return image_uri;
    }

    public void setImage_uri(Uri image_uri)
    {
        this.image_uri = image_uri;
    }
}
