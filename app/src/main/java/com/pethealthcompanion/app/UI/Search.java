package com.pethealthcompanion.app.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.dao.PetDAO;
import com.pethealthcompanion.app.database.AppointmentDatabaseBuilder;
import com.pethealthcompanion.app.database.Repository;
import com.pethealthcompanion.app.entities.Pet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Search extends AppCompatActivity {
    private Repository repository;
    private SearchPetAdapter searchPetAdapter;
    private List<Pet> allSearchedPets = new ArrayList<>();
    private String previousSearchQuery = "";
    private LiveData<List<Pet>> searchResults;

    String petName;
    EditText editPetName;
    private PetDAO petDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button searchPetButton = findViewById(R.id.searchPetButton);
        searchPetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                performPetSearchAndGenerateReport();
            }
        });

        editPetName = findViewById(R.id.searchPetName);
        petName = getIntent().getStringExtra("petName");
        editPetName.setText(petName);

        petDAO = AppointmentDatabaseBuilder.getDatabase(this).petDAO();

        repository = new Repository(getApplication());

        RecyclerView petsRecyclerView2 = findViewById(R.id.pets_recyclerview2);

        searchPetAdapter = new SearchPetAdapter(this);
        petsRecyclerView2.setAdapter(searchPetAdapter);
        petsRecyclerView2.setLayoutManager(new LinearLayoutManager(this));

        searchResults = repository.searchPetsByName(editPetName.getText().toString());

    }

    private void performPetSearchAndGenerateReport() {
        EditText editPetName = findViewById(R.id.searchPetName);
        String searchQuery = editPetName.getText().toString().trim();

        Log.d("SearchActivity", "performPetSearch() method called with search query: " + searchQuery);

        if (searchResults != null) {
            searchResults.removeObservers(this);
        }

        searchResults = repository.searchPetsByName(searchQuery);
        searchResults.observe(this, pets -> {
            if (pets != null && pets.size() > 0) {
                Log.d("SearchActivity", "Search results size: " + pets.size());

                List<Pet> newSearchResults = new ArrayList<>(pets);
                searchPetAdapter.setPets(newSearchResults);
                allSearchedPets.addAll(pets);

                generateReport();
            } else {
                Log.d("SearchActivity", "No search results found");
                Toast.makeText(Search.this, "No search results found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateReport() {
        String dateTimeStamp = getCurrentDateTimeStamp();
        String reportTitle = "Search Results Report";

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("Pet Information\n\n");

        if (allSearchedPets.isEmpty()) {
            Toast.makeText(this, "Pet not found.", Toast.LENGTH_SHORT).show();
        } else {
            for (Pet pet : allSearchedPets) {
                reportContent.append(pet.getPetName())
                        .append("  -  ")
                        .append(pet.getSpecies())
                        .append("  -  ")
                        .append(pet.getBreed())
                        .append("\n");
            }
        }

        String fullReport = "Report Title: " + reportTitle + "\n"
                + "Date-Time Stamp: " + dateTimeStamp + "\n\n"
                + reportContent.toString();

        TextView reportTextView = findViewById(R.id.reportTextView);
        reportTextView.setText(fullReport);
    }


    private String getCurrentDateTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
