package com.java.uidemo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notifications")
public class DemoNotification
{
    @PrimaryKey
    @ColumnInfo(name = "notification_id")
    int id;

    @ColumnInfo(name = "notification_title")
    String title;

    @ColumnInfo(name = "notification_content")
    String content;

    @ColumnInfo(name = "notification_date")
    Date date;

    public DemoNotification()
    {
        id = 0;
        title = "";
        content = "";
        date = new Date(0);
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getContent()
    {
        return content;
    }

    public Date getDate()
    {
        return date;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
