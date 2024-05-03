package com.pethealthcompanion.app.entities;

public class AnnualCheckupAppointment extends Appointment implements AppointmentType {

    public AnnualCheckupAppointment(int appointmentID, String petName, String appointmentType, String veterinaryClinic, String notes, String appointmentDate, String appointmentTime) {
        super(appointmentID, petName, appointmentType, veterinaryClinic, notes, appointmentDate, appointmentTime);
    }

    public AnnualCheckupAppointment() {
        super();
    }

    @Override
    public String getType() {
        return "Annual Checkup";
    }

    @Override
    public String toString() {
        return getType();
    }
}
