package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.openclassrooms.realestatemanager.utils.MyApp;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {
    WifiManager wifiManager = (WifiManager) MyApp.getContext().getSystemService(Context.WIFI_SERVICE);

    @Test
    public void isOnlineWifiTest() {

        wifiManager.setWifiEnabled(true);

        Boolean isConnected = Utils.isOnline();
        assertEquals(true,isConnected);
    }
    //@Test
    //public void isNotOnlineWifiTest() {
    //
//
    //    wifiManager.setWifiEnabled(false);
    //    Boolean isNotConnected = Utils.isOnline();
    //    assertEquals(false,isNotConnected);
    //}


}
