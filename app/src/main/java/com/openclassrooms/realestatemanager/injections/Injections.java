package com.openclassrooms.realestatemanager.injections;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.repositories.PhotoDataRepository;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injections {

    public static RealEstateDataRepository provideRealEstateDataSource(Context context) {
        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new RealEstateDataRepository(database.realEstateDao());
    }

    public static PhotoDataRepository providePhotoDataSource(Context context) {
        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new PhotoDataRepository(database.photoDao());
    }


    public static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        RealEstateDataRepository realEstateDataSource = provideRealEstateDataSource(context);
        PhotoDataRepository photoDataSource = providePhotoDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(realEstateDataSource,photoDataSource, executor);
    }
}
