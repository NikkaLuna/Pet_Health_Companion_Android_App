package com.pethealthcompanion.app.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.ParseException;
import androidx.lifecycle.Observer;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.database.Repository;
import com.pethealthcompanion.app.entities.Pet;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PetProfile extends AppCompatActivity {

    Repository repository;
    int petID;
    String petName;
    String species;
    String breed;
    String birthday;

    EditText editPetName;
    EditText editSpecies;
    EditText editBreed;
    EditText editBirthday;
    Pet currentPet;

    DatePickerDialog.OnDateSetListener petBirthday;
    final Calendar myCalendarBirthday = Calendar.getInstance();
    private int lastPetID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        repository = new Repository(getApplication());

        petID = getIntent().getIntExtra("petID", -1);
        petName = getIntent().getStringExtra("petName");
        species = getIntent().getStringExtra("species");
        breed = getIntent().getStringExtra("breed");
        birthday = getIntent().getStringExtra("birthday");


        editPetName = findViewById(R.id.petName);
        editSpecies = findViewById(R.id.species);
        editBreed = findViewById(R.id.breed);
        editBirthday = findViewById(R.id.birthday);


        editPetName.setText(petName);
        editSpecies.setText(species);
        editBreed.setText(breed);
        editBirthday.setText(birthday);


        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String appointmentStartDate = getIntent().getStringExtra("startDate");

        petBirthday = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarBirthday.set(Calendar.YEAR, year);
                myCalendarBirthday.set(Calendar.MONTH, monthOfYear);
                myCalendarBirthday.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(editBirthday, myCalendarBirthday);
            }
        };

        String startDateStr = getIntent().getStringExtra("startDate");

        if (startDateStr != null && !startDateStr.isEmpty()) {
            try {
                Date date = sdf.parse(startDateStr);
                Log.d(getApplicationContext().getPackageName(), "Parsed start date: " + date);
                myCalendarBirthday.setTime(date);
                updateLabel(editBirthday, myCalendarBirthday);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar todayCalendar = Calendar.getInstance();
                todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
                todayCalendar.set(Calendar.MINUTE, 0);
                todayCalendar.set(Calendar.SECOND, 0);
                todayCalendar.set(Calendar.MILLISECOND, 0);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PetProfile.this, petBirthday, myCalendarBirthday
                        .get(Calendar.YEAR), myCalendarBirthday.get(Calendar.MONTH),
                        myCalendarBirthday.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(todayCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }


    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendarBirthday.getTime()));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pet_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        final Pet pet;
        if (itemId == R.id.petSave) {
            if (petID == -1) {
                repository.getMaxPetID(new Repository.OnMaxPetIDCallback() {
                    @Override
                    public void onMaxPetIDReceived(int maxPetID) {
                        int newPetID = maxPetID + 1;
                        Pet newPet = new Pet(newPetID, editPetName.getText().toString(), editSpecies.getText().toString(), editBreed.getText().toString(), editBirthday.getText().toString());
                        repository.insert(newPet);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PetProfile.this, "Your pet '" + editPetName.getText().toString() + "' was saved.", Toast.LENGTH_LONG).show();
                            }
                        });
                        finish();
                    }
                });
            } else {
                pet = new Pet(petID, editPetName.getText().toString(), editSpecies.getText().toString(), editBreed.getText().toString(), editBirthday.getText().toString());
                repository.update(pet);
                Toast.makeText(PetProfile.this, "Your pet '" + editPetName.getText().toString() + "' was updated.", Toast.LENGTH_LONG).show();
                finish();
            }
            return true;
        }



        if (itemId == R.id.petDelete) {
            repository.getAllPets().observe(this, new Observer<List<Pet>>() {
                @Override
                public void onChanged(List<Pet> pets) {
                    Pet petToDelete = null;
                    for (Pet pet : pets) {
                        if (pet.getPetID() == petID) {
                            petToDelete = pet;
                            break;
                        }
                    }

                    if (petToDelete != null) {
                        repository.delete(petToDelete);
                        Toast.makeText(PetProfile.this, petToDelete.getPetName() + " was deleted.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }


        if (item.getItemId() == R.id.notify) {
            String dateFromScreen = editBirthday.getText().toString();
            String petName = getIntent().getStringExtra("petName");

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }

            Long trigger = myDate.getTime();
            Intent intent = new Intent(PetProfile.this, MyReceiver.class);
            intent.putExtra("key", "Your pet's " + petName + " birthday is today, " + birthday + ".");


            int pendingIntentId = generateRandomNumber();

            PendingIntent sender = PendingIntent.getBroadcast(PetProfile.this, pendingIntentId, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

            return true;
        }


        if (itemId == R.id.share) {
            StringBuilder shareText = new StringBuilder();
            shareText.append("Pet Name: ").append(editPetName.getText().toString()).append("\n");
            shareText.append("Species: ").append(editSpecies.getText().toString()).append("\n");
            shareText.append("Breed: ").append(editBreed.getText().toString()).append("\n");
            shareText.append("Birthday: ").append(editBirthday.getText().toString()).append("\n");


            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int generateRandomNumber () {
        Random random = new Random();
        return random.nextInt(1000);
    }

}