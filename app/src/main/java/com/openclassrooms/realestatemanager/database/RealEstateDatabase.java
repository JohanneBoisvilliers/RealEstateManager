package com.openclassrooms.realestatemanager.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.database.dao.UserDao;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.User;


@Database(entities = {RealEstate.class, User.class, Photo.class}, version = 1, exportSchema = false)
public abstract class RealEstateDatabase extends RoomDatabase {
    // SINGLETON
    private static volatile RealEstateDatabase INSTANCE;

    //DAO
    public abstract RealEstateDao realEstateDao();
    public abstract PhotoDao photoDao();
    public abstract UserDao userDao();

    // --- INSTANCE ---
    public static RealEstateDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues realEstateTest = new ContentValues();
                realEstateTest.put("category", "Loft");
                realEstateTest.put("price",200000);
                realEstateTest.put("description","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
                realEstateTest.put("surface",120);
                realEstateTest.put("nbreOfRoom",10);
                realEstateTest.put("userId",1);
                realEstateTest.put("isSold",false);
                db.insert("RealEstate", OnConflictStrategy.IGNORE, realEstateTest);

                ContentValues userTest = new ContentValues();
                userTest.put("id",1);
                userTest.put("username","Johanne Boisvilliers");
                userTest.put("password","utilisateurDeBase");
                db.insert("User",OnConflictStrategy.IGNORE, userTest);

                ContentValues photoTest = new ContentValues();
                photoTest.put("id",1);
                photoTest.put("realEstateId",1);
                photoTest.put("url","/storage/emulated/0/DCIM/1.jpg");
                db.insert("Photo",OnConflictStrategy.IGNORE, photoTest);
            }
        };
    }
}
