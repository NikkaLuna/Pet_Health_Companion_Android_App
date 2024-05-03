package com.pethealthcompanion.app.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets")
public class Pet {

    @PrimaryKey(autoGenerate = true)
    private int petID;
    private String petName;
    public String species;
    public String breed;
    public String birthday;

    public Pet(int petID, String petName, String species, String breed, String birthday) {
        this.petID = petID;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.birthday = birthday;
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
