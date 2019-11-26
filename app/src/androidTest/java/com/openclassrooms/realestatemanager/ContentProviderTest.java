package com.openclassrooms.realestatemanager;

import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.provider.RealEstateProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest {
    private RealEstateDatabase database;

    // FOR DATA
    private ContentResolver mContentResolver;

    // DATA SET FOR TEST
    private static long USER_ID = 1;

    @Before
    public void setUp() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetItem() {
        // BEFORE : Adding demo item
        final Uri userUri = mContentResolver.insert(RealEstateProvider.URI_ITEM, generateItem());
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(RealEstateProvider.URI_ITEM, USER_ID), null, null, null, null);
        assertThat(cursor, notNullValue());
        assertThat(cursor.getCount(), is(1));
        assertThat(cursor.moveToFirst(), is(true));
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("id")), is("1"));
        cursor.close();
    }

    private ContentValues generateItem() {
        final ContentValues values = new ContentValues();
        values.put("id", "1");
        values.put("userId", "1");
        values.put("category", "house");
        values.put("price", "250000");
        values.put("isSold", "false");
        values.put("surface", "150");
        values.put("nbreOfRoom", "5");
        values.put("description", "bla");
        values.put("address", "20 rue test,38000, blabla");
        values.put("pointsOfInterest", "shop");
        values.put("upForSale", "false");
        values.put("soldSince", "null");
        return values;
    }
}
