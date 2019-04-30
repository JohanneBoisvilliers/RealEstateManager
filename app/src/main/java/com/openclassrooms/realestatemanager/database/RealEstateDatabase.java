package com.openclassrooms.realestatemanager.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.User;

@Database(entities = {RealEstate.class, User.class}, version = 1, exportSchema = false)
public abstract class RealEstateDatabase extends RoomDatabase {
}
