package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.RealEstate;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateTest {

    // FOR DATA
    private RealEstateDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }


    private static RealEstate NEW_ITEM_PLACE_TO_VISIT = new RealEstate();
    private static RealEstate NEW_ITEM_IDEA = new RealEstate();
    private static RealEstate NEW_ITEM_RESTAURANTS = new RealEstate();


    @Test
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<RealEstate> items = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstates());
        assertTrue(items.isEmpty());
    }

}
