package com.pethealthcompanion.app.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pethealthcompanion.app.entities.Pet;

import java.util.List;

@Dao
public interface PetDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);

    @Query("SELECT MAX(petID) FROM pets")
    int getLastPetID();

    @Query("SELECT * FROM pets")
    LiveData<List<Pet>> getAllPets();

    @Query("SELECT * FROM pets WHERE petID = :petID")
    Pet getPetById(int petID);

    @Query("SELECT petName FROM pets")
    LiveData<List<String>> getAllPetNames();

    @Query("SELECT * FROM pets WHERE petName LIKE :searchQuery")
    LiveData<List<Pet>> searchPetsByName(String searchQuery);

    @Query("DELETE FROM pets WHERE petName = :petName")
    void delete(String petName);

}
