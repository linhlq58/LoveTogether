package com.valentineapp.lovetogether.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by lequy on 2/3/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static BaseActivity INSTANCE;

    protected abstract int getLayoutResource();

    protected abstract void initVariables(Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    private int currentApiVersion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        INSTANCE = this;
        setContentView(getLayoutResource());
        initVariables(savedInstanceState);
        initData(savedInstanceState);

    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * Start Activity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void startActivity(Class clazz, Bundle bundle) {
        try {
            Intent intent = new Intent(this, clazz);
            if (bundle != null)
                intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void startActivityWithAnimation(Class clazz, Bundle bundle) {
        try {
            Intent intent = new Intent(this, clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, bundle);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start Activity without bundle
     *
     * @param clazz
     */
    protected void startActivity(Class clazz) {
        startActivity(clazz, null);
    }

    /**
     * Change new Fragment content inside layout
     * @param fragment - new Fragment
     * @param layoutId - target layout
     */
    protected  void changeFragment(Fragment fragment, int layoutId){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(layoutId, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
