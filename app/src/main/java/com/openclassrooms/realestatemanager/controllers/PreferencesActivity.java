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

    public static class MyPreferenceFragment extends PreferenceFragment /*implements SharedPreferences.OnSharedPreferenceChangeListener*/ {

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());

        //@Override
        //public void onResume() {
        //    super.onResume();
        //    for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
        //        Preference preference = getPreferenceScreen().getPreference(i);
        //        if (preference instanceof PreferenceGroup) {
        //            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
        //            for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
        //                Preference singlePref = preferenceGroup.getPreference(j);
        //                getValueForCurrency(singlePref);
        //            }
        //        } else {
        //            getValueForCurrency(preference);
        //        }
        //    }
        //}

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            //getPreferenceScreen().getSharedPreferences()
            // .registerOnSharedPreferenceChangeListener(this);
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

//        @Override
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            if (key.equals("realEstateCurrency")) {
//                return;
//            }else{
//                this.getValueForCurrency(findPreference(key));
//            }
//        }

        private void getValueForCurrency(String newValue) {
            CharSequence realEstateCurrencyChosen = newValue;
            String currency;
            if (realEstateCurrencyChosen.equals("1")) {
                currency = "Dollars";
            } else {
                currency = "Euros";
            }
            sharedPreferences.edit().putString("realEstateCurrency",
                    currency).apply();
        }
    }
}
