package com.pethealthcompanion.app.entities;

import android.app.Application;

import androidx.room.Room;

import com.pethealthcompanion.app.dao.UserDAO;
import com.pethealthcompanion.app.database.AppointmentDatabaseBuilder;

public class MyApplication extends Application {
    private static MyApplication instance;
    private AppointmentDatabaseBuilder database;
    private UserDAO userDao;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppointmentDatabaseBuilder database = Room.databaseBuilder(getApplicationContext(), AppointmentDatabaseBuilder.class, "AppointmentDatabaseBuilder").build();
        userDao = database.userDAO();
    }
    public static MyApplication getInstance() {
        return instance;
    }

    public UserDAO getUserDao() {
        return userDao;
    }
}
