package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilsUnitTest {
    @Test
    public void convertEuroToDollarTest() throws Exception {
        int result = Utils.convertEuroToDollars(25);
        assertEquals(28, result);
    }

    @Test
    public void convertDollarToEuroTest() throws Exception {
        int result = Utils.convertDollarToEuro(25);
        assertEquals(22, result);
    }

    @Test
    public void getTodayDateTest() throws Exception {
        Boolean isGoodFormat=false;
        String actualDate = Utils.getTodayDate();
        String formatDate = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        sdf.setLenient(false);

        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(actualDate);
            isGoodFormat = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(isGoodFormat);
    }
}
