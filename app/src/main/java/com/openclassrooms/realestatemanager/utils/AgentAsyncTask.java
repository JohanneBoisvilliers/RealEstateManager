package com.openclassrooms.realestatemanager.utils;

import android.os.AsyncTask;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.database.dao.UserDao;

import java.lang.ref.WeakReference;

public class AgentAsyncTask extends AsyncTask<Void, Void, Long> {

    private final WeakReference<Listeners> callback;
    private String username;
    private String password;

    public AgentAsyncTask(Listeners callback, String username, String password) {
        this.callback = new WeakReference<>(callback);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callback.get().onPreExecute();
    }

    @Override
    protected void onPostExecute(Long success) {
        super.onPostExecute(success);
        this.callback.get().onPostExecute(success);
    }

    @Override
    protected Long doInBackground(Void... voids) {
        this.callback.get().doInBackground();
        RealEstateDatabase database = RealEstateDatabase.getInstance(MyApp.getContext());
        UserDao userDao = database.userDao();
        return userDao.getUserForSignIn(username, password);
    }

    public interface Listeners {
        void onPreExecute();

        void doInBackground();

        void onPostExecute(Long success);
    }
}
