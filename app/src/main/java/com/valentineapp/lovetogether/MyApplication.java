package com.valentineapp.lovetogether;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mz.ZAndroidSystemDK;
import com.valentineapp.lovetogether.database.DatabaseHelper;

/**
 * Created by lequy on 2/6/2017.
 */

public class MyApplication extends Application {
    private static SharedPreferences sharedPreferences;
    private static DatabaseHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        ZAndroidSystemDK.initApplication(this, this.getApplicationContext().getPackageName());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = new DatabaseHelper(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static DatabaseHelper getDb() {
        return db;
    }
}
