package com.openclassrooms.realestatemanager.utils;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.controllers.RealEstateOnMapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class ConvertAddressesAsyncTask extends AsyncTask<Void, Void, String[]> {

    private final WeakReference<ConvertAddressesAsyncTask.Listeners> mCallback;
    private LinkedHashMap<Long, String> mAddresses;
    private LinkedHashMap<Long, LatLng> mTempLatLngList = new LinkedHashMap<>();
    private String mApiKey;
    private RealEstateOnMapActivity mRealEstateOnMapActivity;

    public ConvertAddressesAsyncTask(RealEstateOnMapActivity activity, Listeners callback,
                                     LinkedHashMap<Long, String> listOfAddresses,
                                     String apiKey) {
        this.mRealEstateOnMapActivity = activity;
        this.mAddresses = listOfAddresses;
        this.mApiKey = apiKey;
        this.mCallback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.mCallback.get().onPreExecute();
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        String response;
        ArrayList<String> tempList = new ArrayList<>();
        this.mCallback.get().doInBackground();
        try {
            Iterator it = mAddresses.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                response = getLatLongByURL("https://maps.googleapis" + ".com/maps/api/geocode/json?address" +
                        "=" +
                        pair.getValue() + "&key=" + mApiKey);
                tempList.add(response);
            }
            String[] tempArray = new String[mAddresses.size()];
            tempArray = tempList.toArray(tempArray);
            return tempArray;
        } catch (Exception e) {
            return new String[]{"error"};
        }
    }

    @Override
    protected void onPostExecute(String... result) {
        try {
            for (int i = 0; i < result.length; i++) {
                JSONObject jsonObject = new JSONObject(result[i]);
                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                LatLng tempLatLng = new LatLng(lat, lng);
                Set<Map.Entry<Long, String>> mapSet = mAddresses.entrySet();
                Map.Entry<Long, String> elementAtPosition =
                        (Map.Entry<Long, String>) mapSet.toArray()[i];
                mTempLatLngList.put(elementAtPosition.getKey(), tempLatLng);
            }
            mRealEstateOnMapActivity.setLatLngsAddresses(mTempLatLngList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.mCallback.get().onPostExecute();
    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public interface Listeners {
        void onPreExecute();

        void doInBackground();

        void onPostExecute();
    }
}