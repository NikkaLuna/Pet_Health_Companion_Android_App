package com.pethealthcompanion.app.entities;

public class AppointmentFactory {
    public static AppointmentType[] getAppointmentTypes() {
        return new AppointmentType[] {
                new AnnualCheckupAppointment(),
                new DentalCleaningAppointment(),
                new AccidentIllnessAppointment()
        };
    }
}
