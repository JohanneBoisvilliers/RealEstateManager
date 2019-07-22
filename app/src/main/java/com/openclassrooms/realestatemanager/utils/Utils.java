package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    //TODO pourquoi pas essayer d'utiliser une api pour taux actuel
    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.89);
    }

    public static int convertEuroToDollars(int euros){
        return (int) Math.round(euros * 1.12);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param
     * @return
     */
    public static boolean isOnline() {
        Context context = MyApp.getContext();
        if (Connectivity.isConnectedWifi(context)) {
            return true;
        }else{
            return Connectivity.isConnectedMobile(context) && Connectivity.isConnectedFast(context);
        }
    }

    // Centralize creation of application's snackbars
    public static void showSnackBar(@NonNull View coordinator, String textToShow, int duration) {
        Snackbar snackbar = Snackbar.make(coordinator, textToShow, duration);
        snackbar.show();
    }

}
