package com.pethealthcompanion.app.entities;

public class AccidentIllnessAppointment extends Appointment implements AppointmentType {

    public AccidentIllnessAppointment(int appointmentID, String petName, String appointmentType, String veterinaryClinic, String notes, String appointmentDate, String appointmentTime) {
        super(appointmentID, petName, appointmentType, veterinaryClinic, notes, appointmentDate, appointmentTime);
    }


    public AccidentIllnessAppointment() {
        super();
    }


    @Override
    public String getType() {
        return "Accident/Illness";
    }

    @Override
    public String toString() {
        return getType();
    }
}