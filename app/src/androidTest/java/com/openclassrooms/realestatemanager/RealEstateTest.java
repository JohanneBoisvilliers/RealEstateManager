package com.openclassrooms.realestatemanager;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RealEstateTest {

    // FOR DATA
    private RealEstateDatabase database;
    private static RealEstate mRealEstate1 = new RealEstate(1, 1);
    private static RealEstate mRealEstate2 = new RealEstate(2, 1);
    private static RealEstate mRealEstate3 = new RealEstate(3, 1);
    private static User mUser = new User();

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

    @Test
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<RealEstate> items = LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstates());
        assertTrue(items.isEmpty());
    }

    @Test
    public void insertAndGetEstate() throws InterruptedException {
        // TEST
        mUser.setId(1);
        this.database.userDao().insertUser(mUser);
        this.database.realEstateDao().insertRealEstate(mRealEstate1);
        this.database.realEstateDao().insertRealEstate(mRealEstate2);
        this.database.realEstateDao().insertRealEstate(mRealEstate3);

        List<RealEstate> items =
                LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstates());
        assertEquals(3, items.size());
    }

    @Test
    public void updateAndGetEstate() throws InterruptedException {
        // TEST
        mUser.setId(1);
        this.database.userDao().insertUser(mUser);
        this.database.realEstateDao().insertRealEstate(mRealEstate1);
        mRealEstate1.setSold(true);
        this.database.realEstateDao().updateRealEstate(mRealEstate1);

        List<RealEstate> items =
                LiveDataTestUtil.getValue(this.database.realEstateDao().getRealEstates());
        assertTrue(items.get(0).getSold());
    }

}
