package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.openclassrooms.realestatemanager.models.User;

@Dao
public interface UserDao {
    @Query("SELECT  User.id FROM User WHERE User.username = :username AND User.password = " +
            ":password")
    Long getUserForSignIn(String username, String password);

    @Query("SELECT * FROM User WHERE User.id = :userId")
    LiveData<User> getUser(Long userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);
}
