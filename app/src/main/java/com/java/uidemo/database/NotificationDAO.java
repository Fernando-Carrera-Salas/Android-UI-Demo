package com.java.uidemo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.java.uidemo.model.DemoNotification;

import java.util.List;

@Dao
public interface NotificationDAO
{
    @Query("SELECT * FROM notifications ORDER BY notification_date DESC")
    List<DemoNotification> getAll();

    @Query("SELECT * FROM notifications ORDER BY notification_id DESC LIMIT 1")
    List<DemoNotification> getLast();

    @Query("SELECT * FROM notifications WHERE notification_id IN (:ids)")
    List<DemoNotification> getById(int[] ids);

    @Insert
    void insertAll(DemoNotification... notifications);

    @Delete
    void delete(DemoNotification notification);
}
