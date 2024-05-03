package com.pethealthcompanion.app.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.database.Repository;
import com.pethealthcompanion.app.entities.Appointment;
import com.pethealthcompanion.app.entities.Pet;

import java.util.List;

public class AppointmentList extends AppCompatActivity {

    private Repository repository;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appointment_list, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentList.this, AppointmentDetails.class);
                startActivity(intent);
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentList.this, PetProfile.class);
                startActivity(intent);
            }
        });

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentList.this, Search.class);
                startActivity(intent);
            }
        });

        repository = new Repository(getApplication());

        RecyclerView petsRecyclerView = findViewById(R.id.pets_recyclerview);
        final PetAdapter petAdapter = new PetAdapter(this);

        repository.getAllPets().observe(this, pets -> {
            petAdapter.setPets(pets);
        });

        petsRecyclerView.setAdapter(petAdapter);
        petsRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Appointment> allAppointments = repository.getAllAppointments();
        final AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentAdapter.setAppointments(allAppointments);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            this.finish();
            return true;
        }

        if(item.getItemId()==R.id.sample) {
            repository=new Repository(getApplication());
            Appointment appointment=new Appointment(0, "Garfield", "Dental Cleaning", "Animal Heart Hospital", "Garfield will stay all day", "04/20/24", "2:00pm");
            repository.insert(appointment);
            appointment=new Appointment(0, "Buffy", "Annual Bloodwork", "Creature Comforts", "Fast 12 hours before appointment", "04/19/24", "1:00pm");
            repository.insert(appointment);
            Pet pet=new Pet(0,"Muffin", "Cat", "Siamese", "02/16/24");
            repository.insert(pet);
            pet=new Pet(0,"Buttons", "Dog", "German Shepard", "01/02/2024");
            repository.insert(pet);
            return true;
        }
        if (itemId == R.id.logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Toast.makeText(this, "You have been logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Repository repository = new Repository(getApplication());

        final PetAdapter petAdapter = new PetAdapter(this);

        repository.getAllPets().observe(this, pets -> {
            petAdapter.setPets(pets);
        });

        RecyclerView petsRecyclerView = findViewById(R.id.pets_recyclerview);
        petsRecyclerView.setAdapter(petAdapter);
        petsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        List<Appointment> allAppointments = repository.getAllAppointments();
        final AppointmentAdapter appointmentAdapter = new AppointmentAdapter(this);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentAdapter.setAppointments(allAppointments);
    }


}