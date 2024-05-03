package com.pethealthcompanion.app.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "appointments")
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    private int appointmentID;
    private String petName;
    public String appointmentType;
    public String veterinaryClinic;
    private String notes;
    public String appointmentDate;
    public String appointmentTime;

    public Appointment(int appointmentID, String petName, String appointmentType, String veterinaryClinic,
                       String notes, String appointmentDate, String appointmentTime) {
        this.appointmentID = appointmentID;
        this.petName = petName;
        this.appointmentType = appointmentType;
        this.veterinaryClinic = veterinaryClinic;
        this.notes = notes;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public Appointment() {

    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getVeterinaryClinic() {
        return veterinaryClinic;
    }

    public void setVeterinaryClinic(String veterinaryClinic) {
        this.veterinaryClinic = veterinaryClinic;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
