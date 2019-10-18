package com.openclassrooms.realestatemanager.controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.MyApp;

public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            ListPreference listPreference = (ListPreference) findPreference("currency");
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    listPreference.setValue((String) newValue);
                    getValueForCurrency((String) newValue);
                    return false;
                }
            });


        }

        //set the currency preference
        private void getValueForCurrency(String newValue) {
            String currency;
            if (((CharSequence) newValue).equals("1")) {
                currency = "Dollars";
            } else {
                currency = "Euros";
            }
            sharedPreferences.edit().putString("realEstateCurrency",
                    currency).apply();
        }
    }
}
