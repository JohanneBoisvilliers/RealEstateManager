package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

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

    //load image into header with glide
    public static void configureImageHeader(Context context, ImageView view) {
        Glide.with(context)
                .load(context.getResources().getDrawable(R.drawable.gradient_bleu))
                .centerCrop()
                .into(view);
    }

    //load image into header with glide
    public static void configureUserPhoto(@Nullable Object url, Context context, ImageView imageView) {
        if (url == null) {
            url = context.getResources().getDrawable(R.drawable.user);
        }
        Glide.with(context)
                .load(url)
                .circleCrop()
                .into(imageView);
    }

}
