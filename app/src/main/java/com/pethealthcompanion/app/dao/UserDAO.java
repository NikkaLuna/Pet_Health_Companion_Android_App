package com.pethealthcompanion.app.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pethealthcompanion.app.entities.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users WHERE username = :username")
    LiveData<User> getUserByUsernameLiveData(String username);

    @Insert
    void insert(User user);
}



