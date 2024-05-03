package com.pethealthcompanion.app.entities;

public class DentalCleaningAppointment extends Appointment implements AppointmentType {

    public DentalCleaningAppointment(int appointmentID, String petName, String appointmentType, String veterinaryClinic, String notes, String appointmentDate, String appointmentTime) {
        super(appointmentID, petName, appointmentType, veterinaryClinic, notes, appointmentDate, appointmentTime);
    }

    public DentalCleaningAppointment() {
        super();
    }

    @Override
    public String getType() {
        return "Dental Cleaning";
    }

    @Override
    public String toString() {
        return getType();
    }
}
