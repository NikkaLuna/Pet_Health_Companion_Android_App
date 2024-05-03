package com.pethealthcompanion.app.UI;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ParseException;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.dao.AppointmentDAO;
import com.pethealthcompanion.app.dao.PetDAO;
import com.pethealthcompanion.app.database.AppointmentDatabaseBuilder;
import com.pethealthcompanion.app.database.Repository;
import com.pethealthcompanion.app.entities.Appointment;
import com.pethealthcompanion.app.entities.AppointmentFactory;
import com.pethealthcompanion.app.entities.AppointmentType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class AppointmentDetails extends AppCompatActivity {

    Repository repository;

    int appointmentID;
    String petName;
    String appointmentType;
    String veterinaryClinic;
    String notes;
    String appointmentDate;
    String appointmentTime;


    EditText editPetName;
    EditText editAppointmentType;
    EditText editVeterinaryClinic;
    EditText editNotes;
    EditText editAppointmentDate;
    EditText editAppointmentTime;
    Appointment currentAppointment;

    private static final int APPOINTMENT_START_PENDING_INTENT_ID = 1;
    private Spinner appointmentTypeSpinner;

    private Date appointmentScheduledDate = new Date();

    private Calendar myCalendarAppointment = Calendar.getInstance();
    private PetDAO petDAO;
    private AppointmentDAO appointmentDAO;
    private AppointmentDatabaseBuilder appointmentDatabase;


    public AppointmentDetails(Repository repository) {
        this.repository = repository;
    }

    public AppointmentDetails() {
    }

    private Appointment getAppointmentDetails(int appointmentId) {
        return appointmentDAO.getAppointmentById(appointmentId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        repository = new Repository(getApplication());

        editPetName = findViewById(R.id.petName);
        Spinner editAppointmentType = findViewById(R.id.appointment_type_spinner);



        editVeterinaryClinic = findViewById(R.id.clinic);
        editNotes = findViewById(R.id.notes);
        editAppointmentDate = findViewById(R.id.appointmentDate);
        editAppointmentTime = findViewById(R.id.appointmentTime);


        appointmentID = getIntent().getIntExtra("appointmentID", -1);
        petName = getIntent().getStringExtra("petName");
        appointmentType = getIntent().getStringExtra("appointmentType");
        veterinaryClinic = getIntent().getStringExtra("veterinaryClinic");

        notes = getIntent().getStringExtra("notes");
        appointmentDate = getIntent().getStringExtra("appointmentDate");
        appointmentTime = getIntent().getStringExtra("appointmentTime");

        editPetName.setText(petName);
        editVeterinaryClinic.setText(veterinaryClinic);
        editNotes.setText(notes);

        editAppointmentDate.setText(appointmentDate);


        appointmentTypeSpinner = findViewById(R.id.appointment_type_spinner);

        AppointmentType[] appointmentTypes = AppointmentFactory.getAppointmentTypes();
        ArrayAdapter<AppointmentType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, appointmentTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appointmentTypeSpinner.setAdapter(adapter);

        for (int i = 0; i < appointmentTypes.length; i++) {
            if (appointmentTypes[i].getType().equals(appointmentType)) {
                appointmentTypeSpinner.setSelection(i);
                break;
            }
        }


        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String myTimeFormat = "hh:mm a";
        SimpleDateFormat timeFormat = new SimpleDateFormat(myTimeFormat, Locale.US);
        Calendar myCalendarTime = Calendar.getInstance();



        AtomicReference<Date> appointmentScheduledDate = new AtomicReference<>((Date) getIntent().getSerializableExtra("appointmentScheduledDate"));

        if (appointmentScheduledDate.get() != null) {
            myCalendarAppointment.setTime(appointmentScheduledDate.get());
            updateLabel(editAppointmentDate, myCalendarAppointment);
        }

        DatePickerDialog.OnDateSetListener appointmentDateListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendarAppointment.set(Calendar.YEAR, year);
            myCalendarAppointment.set(Calendar.MONTH, monthOfYear);
            myCalendarAppointment.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
            todayCalendar.set(Calendar.MINUTE, 0);
            todayCalendar.set(Calendar.SECOND, 0);
            todayCalendar.set(Calendar.MILLISECOND, 0);

            if (myCalendarAppointment.before(todayCalendar)) {
                Toast.makeText(AppointmentDetails.this, "Appointment Date cannot be a date in the past.", Toast.LENGTH_SHORT).show();
            } else {
                appointmentScheduledDate.set(myCalendarAppointment.getTime());
                updateLabel(editAppointmentDate, myCalendarAppointment);
            }
        };



        editAppointmentDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentDetails.this, appointmentDateListener,
                    myCalendarAppointment.get(Calendar.YEAR), myCalendarAppointment.get(Calendar.MONTH),
                    myCalendarAppointment.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });




        if (appointmentTime != null && !appointmentTime.isEmpty()) {
            try {
                Date time = timeFormat.parse(appointmentTime);
                myCalendarTime.setTime(time);
                updateLabel(editAppointmentTime, myCalendarTime);
                editAppointmentTime.setText(appointmentTime);
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
        }


        editAppointmentTime.setOnClickListener(v -> {
            int hour = myCalendarTime.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendarTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentDetails.this,
                    (view, hourOfDay, minuteOfDay) -> {
                        myCalendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendarTime.set(Calendar.MINUTE, minuteOfDay);
                        updateTimeLabel(editAppointmentTime, myCalendarTime);
                    }, hour, minute, false);

            timePickerDialog.show();
        });
    }

    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeLabel(EditText editText, Calendar calendar) {
        String myTimeFormat = "hh:mm a";
        SimpleDateFormat timeFormat = new SimpleDateFormat(myTimeFormat, Locale.US);
        editText.setText(timeFormat.format(calendar.getTime()));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appointment_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }


        if (itemId == R.id.appointmentSave) {
            Appointment appointment;
            if (appointmentID == -1) {
                if (repository.getAllAppointments().size() == 0) appointmentID = 1;
                else
                    appointmentID = repository.getAllAppointments().get(repository.getAllAppointments().size() - 1).getAppointmentID() + 1;

                AppointmentType selectedAppointmentType = (AppointmentType) appointmentTypeSpinner.getSelectedItem();
                appointment = new Appointment(appointmentID, editPetName.getText().toString(), selectedAppointmentType.getType(), editVeterinaryClinic.getText().toString(), editNotes.getText().toString(),editAppointmentDate.getText().toString(), editAppointmentTime.getText().toString());
                repository.insert(appointment);
                Toast.makeText(this, "Your appointment for'" + editPetName.getText().toString() + "' was saved.", Toast.LENGTH_LONG).show();
                this.finish();

            } else {
                AppointmentType selectedAppointmentType = (AppointmentType) appointmentTypeSpinner.getSelectedItem();
                appointment = new Appointment(appointmentID, editPetName.getText().toString(), selectedAppointmentType.getType(), editVeterinaryClinic.getText().toString(), editNotes.getText().toString(),editAppointmentDate.getText().toString(), editAppointmentTime.getText().toString());
                repository.update(appointment);
                Toast.makeText(this, "Your appointment for'" + editPetName.getText().toString() + "' was updated.", Toast.LENGTH_LONG).show();
                this.finish();
            }
            this.finish();
            return true;
        }

        if (itemId == R.id.appointmentDelete) {
            Appointment appointmentToDelete = null;
            for (Appointment appointment : repository.getAllAppointments()) {
                if (appointment.getAppointmentID() == appointmentID) {
                    appointmentToDelete = appointment;
                    break;
                }
            }

            if (appointmentToDelete != null) {
                repository.delete(appointmentToDelete);
                Toast.makeText(AppointmentDetails.this, petName + "'s appointment was deleted.", Toast.LENGTH_LONG).show();

                this.finish();
            }
        }

        else if (itemId == R.id.addSampleNotes) {

            Toast.makeText(AppointmentDetails.this, "Put in sample data", Toast.LENGTH_LONG).show();
        }



        if (item.getItemId() == R.id.notify) {
            String appointmentDateFromScreen = editAppointmentDate.getText().toString();
            String appointmentTimeFromScreen = editAppointmentTime.getText().toString();
            String appointmentName = getIntent().getStringExtra("appointmentName");

            Calendar currentCalendar = Calendar.getInstance();

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            Date appointmentDate = null;

            try {
                appointmentDate = sdf.parse(appointmentDateFromScreen);
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }

            Calendar appointmentCalendar = Calendar.getInstance();

            String[] timeParts = appointmentTimeFromScreen.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes;
            String[] minuteParts = timeParts[1].split(" ");
            if (minuteParts.length == 2) {
                minutes = Integer.parseInt(minuteParts[0]);
                if (minuteParts[1].equalsIgnoreCase("pm")) {
                    hours += 12;
                }
            } else {
                minutes = Integer.parseInt(timeParts[1]);
            }

            appointmentCalendar.set(Calendar.HOUR_OF_DAY, hours);
            appointmentCalendar.set(Calendar.MINUTE, minutes);

            long notificationTrigger = appointmentCalendar.getTimeInMillis();

            boolean isNotificationDate = currentCalendar.get(Calendar.YEAR) == appointmentCalendar.get(Calendar.YEAR) &&
                    currentCalendar.get(Calendar.MONTH) == appointmentCalendar.get(Calendar.MONTH) &&
                    currentCalendar.get(Calendar.DAY_OF_MONTH) == appointmentCalendar.get(Calendar.DAY_OF_MONTH);

            Intent notificationIntent = null;

            if (isNotificationDate) {
                notificationIntent = new Intent(AppointmentDetails.this, MyReceiver.class);
                notificationIntent.putExtra("key", "Your appointment for " + petName + " is scheduled for " + appointmentDateFromScreen + " at " + appointmentTimeFromScreen + ".");



                PendingIntent notificationSender = PendingIntent.getBroadcast(AppointmentDetails.this, APPOINTMENT_START_PENDING_INTENT_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, notificationSender);
            }
            return true;
        } else if (itemId == R.id.addSampleNotes) {

            Toast.makeText(AppointmentDetails.this, "Put in sample data", Toast.LENGTH_LONG).show();
        }

        if (itemId == R.id.share) {
            shareAppointmentDetails();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareAppointmentDetails() {
        String petName = editPetName.getText().toString();
        String appointmentType = getIntent().getStringExtra("appointmentType");
        String veterinaryClinic = editVeterinaryClinic.getText().toString();
        String notes = editNotes.getText().toString();
        String appointmentDate = editAppointmentDate.getText().toString();
        String appointmentTime = editAppointmentTime.getText().toString();


        if (petName.isEmpty() || appointmentType.isEmpty() || veterinaryClinic.isEmpty() || notes.isEmpty() || appointmentDate.isEmpty() || appointmentTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all details before sharing.", Toast.LENGTH_LONG).show();
            return;
        }
        repository.getAllAppointmentsAsync(new Repository.Callback<List<Appointment>>() {

            @Override
            public void onResult(List<Appointment> appointments) {
                Appointment currentAppointment = repository.findAppointmentById(appointments, appointmentID);
                if (currentAppointment != null) {
                    repository.getAllAppointmentsAsync(new Repository.Callback<List<Appointment>>() {

                        @Override
                        public void onResult(List<Appointment> appointments) {
                            Appointment currentAppointment = repository.findAppointmentById(appointments, appointmentID);
                            if (currentAppointment != null) {
                                String shareText = createShareText(petName, appointmentType, veterinaryClinic, String.valueOf(new ArrayList<>()), appointmentDate, appointmentTime, currentAppointment);
                                shareAppointment(shareText);
                            } else {
                                handleShareError("Could not find appointment details", null);
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            handleShareError("Error retrieving associated notes", e);
                        }

                        @Override
                        public void onAppointmentLoaded(Appointment appointment) {

                        }
                    });
                } else {
                    handleShareError("Could not find appointment details", null);
                }
            }
            @Override
            public void onError(Exception e) {
                handleShareError("Error retrieving appointments", e);
            }

            @Override
            public void onAppointmentLoaded(Appointment appointment) {

            }
        });
    }
    private String createShareText(String petName, String appointmentType, String veterinaryClinic, String notes, String appointmentDate, String appointmentTime, Appointment appointment) {
        String appointmentDetails = "Pet Name: " + appointment.getPetName() + "\n" +
                "Appointment Date: " + appointment.getAppointmentDate() + "\n" +
                "Appointment Time: " + appointment.getAppointmentTime() + "\n";


        return "Appointment Details:\n" +
                appointmentDetails +
                "Appointment Type: " + appointmentType + "\n" +
                "Veterinary Clinic: " + veterinaryClinic + "\n";

    }

    private void shareAppointment(String shareText) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void handleShareError(String message, Exception e) {
        if (e != null) {
            Log.e(TAG, message, e);
        } else {
            Log.e(TAG, message);
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}