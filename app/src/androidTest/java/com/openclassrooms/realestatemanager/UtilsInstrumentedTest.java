package com.openclassrooms.realestatemanager;

import android.support.test.runner.AndroidJUnit4;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {

    @Mock
    Utils mUtils = new Utils();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void isOnlineWifiTest() {
        Mockito.when(mUtils.isOnline()).thenReturn(true);
        Boolean isConnected = mUtils.isOnline();
        assertEquals(true,isConnected);
    }

    @Test
    public void isNotOnlineWifiTest() {
        Mockito.when(mUtils.isOnline()).thenReturn(false);
        Boolean isNotConnected = mUtils.isOnline();
        assertEquals(false, isNotConnected);
    }


}
