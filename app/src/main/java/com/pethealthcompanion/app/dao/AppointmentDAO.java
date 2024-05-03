package com.pethealthcompanion.app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pethealthcompanion.app.entities.Appointment;

import java.util.List;

@Dao
public interface AppointmentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("SELECT * FROM APPOINTMENTS ORDER BY appointmentID ASC")
    List<Appointment> getAllAppointments();

    @Query("SELECT * FROM APPOINTMENTS WHERE appointmentID = :appointmentID")
    Appointment getAppointmentById(int appointmentID);

    @Query("SELECT * FROM APPOINTMENTS WHERE petName LIKE :searchQuery")
    LiveData<List<Appointment>> searchPetsByName(String searchQuery);

}
