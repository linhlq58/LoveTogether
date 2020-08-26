package com.valentineapp.lovetogether.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.utils.Constant;

/**
 * Created by lequy on 2/3/2017.
 */

public class SplashActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        sharedPreferences = MyApplication.getSharedPreferences();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!sharedPreferences.getBoolean("password", false)) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(InputPassActivity.class);
                }
                finish();
            }
        }, 2000);
    }
}
