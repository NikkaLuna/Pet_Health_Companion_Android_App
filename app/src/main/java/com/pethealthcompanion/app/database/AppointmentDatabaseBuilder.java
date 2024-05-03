package com.pethealthcompanion.app.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pethealthcompanion.app.dao.AppointmentDAO;
import com.pethealthcompanion.app.dao.PetDAO;
import com.pethealthcompanion.app.dao.UserDAO;
import com.pethealthcompanion.app.entities.Appointment;
import com.pethealthcompanion.app.entities.Pet;
import com.pethealthcompanion.app.entities.User;

@Database(entities = {Appointment.class, Pet.class, User.class}, version=8,exportSchema = false)

public abstract class AppointmentDatabaseBuilder extends RoomDatabase {

    public abstract AppointmentDAO appointmentDAO();
    public abstract PetDAO petDAO();
    public abstract UserDAO userDAO();

    private static volatile AppointmentDatabaseBuilder INSTANCE;

    public static AppointmentDatabaseBuilder getDatabase(final Context context) {
        if(INSTANCE==null){
            synchronized (AppointmentDatabaseBuilder.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AppointmentDatabaseBuilder.class,"AppointmentDatabaseBuilder")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

