package com.java.uidemo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.java.uidemo.model.DemoNotification;

@Database(entities = {DemoNotification.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DemoDatabase extends RoomDatabase
{
    public abstract NotificationDAO notificationDAO();
}