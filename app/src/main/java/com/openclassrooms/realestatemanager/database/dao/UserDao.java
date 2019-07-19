package com.openclassrooms.realestatemanager.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.openclassrooms.realestatemanager.models.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE User.username = :username AND User.password = :password")
    User getUserForSignIn(String username, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);
}
