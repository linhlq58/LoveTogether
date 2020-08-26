package com.valentineapp.lovetogether.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mz.A;
import com.mz.ZAndroidSystemDK;
import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.adapters.MyPagerAdapter;
import com.valentineapp.lovetogether.fragments.MainFragment;
import com.valentineapp.lovetogether.fragments.MemoryFragment;
import com.valentineapp.lovetogether.fragments.SettingsFragment;
import com.valentineapp.lovetogether.services.MyService;
import com.valentineapp.lovetogether.utils.Constant;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private View transView;
    private ImageView bgImg;
    private View shadowView;
    private View shadowView2;
    private View emitter;
    private ImageView memoryButton;
    private TextView titleText;
    private ImageView settingsButton;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ArrayList<Fragment> listFragment;
    private MemoryFragment memoryFragment;
    private MainFragment mainFragment;
    private SettingsFragment settingsFragment;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver wallpaperReceiver;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            } else {
                ZAndroidSystemDK.init(this);
                A.f(this);
                //A.b(this);
            }
        } else {
            ZAndroidSystemDK.init(this);
            A.f(this);
            //A.b(this);
        }

        sharedPreferences = MyApplication.getSharedPreferences();
        transView = findViewById(R.id.trans_view);
        bgImg = (ImageView) findViewById(R.id.bg_img);
        shadowView = findViewById(R.id.shadow_view);
        shadowView2 = findViewById(R.id.shadow_view_2);
        emitter = findViewById(R.id.emitter);
        memoryButton = (ImageView) findViewById(R.id.memory_button);
        titleText = (TextView) findViewById(R.id.title_text);
        settingsButton = (ImageView) findViewById(R.id.settings_button);
        pager = (ViewPager) findViewById(R.id.pager);

        memoryFragment = MemoryFragment.newInstance();
        mainFragment = MainFragment.newInstance();
        settingsFragment = SettingsFragment.newInstance();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        bgImg.setImageResource(sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));

        transView.getLayoutParams().height = Constant.getStatusBarHeight(this);
        transView.requestLayout();

        if (!Constant.isMyServiceRunning(this, MyService.class)) {
            startService(new Intent(getApplicationContext(), MyService.class));
        }

        listFragment = new ArrayList<>();
        listFragment.add(memoryFragment);
        listFragment.add(mainFragment);
        listFragment.add(settingsFragment);

        adapter = new MyPagerAdapter(getSupportFragmentManager(), listFragment);

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        pager.setCurrentItem(1);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        shadowView.setVisibility(View.VISIBLE);
                        shadowView2.setVisibility(View.GONE);
                        memoryButton.setImageResource(R.mipmap.ic_memory);
                        settingsButton.setImageResource(R.mipmap.ic_settings_grey);
                        titleText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        break;
                    case 1:
                        shadowView.setVisibility(View.GONE);
                        shadowView2.setVisibility(View.VISIBLE);
                        memoryButton.setImageResource(R.mipmap.ic_memory_grey);
                        settingsButton.setImageResource(R.mipmap.ic_settings_grey);
                        titleText.setTextColor(getResources().getColor(android.R.color.white));
                        break;
                    case 2:
                        shadowView.setVisibility(View.VISIBLE);
                        shadowView2.setVisibility(View.GONE);
                        memoryButton.setImageResource(R.mipmap.ic_memory_grey);
                        settingsButton.setImageResource(R.mipmap.ic_settings);
                        titleText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        wallpaperReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bgImg.setImageResource(sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));
            }
        };
        registerReceiver(wallpaperReceiver, new IntentFilter(Constant.CHANGE_WALLPAPER));

        memoryButton.setOnClickListener(this);
        titleText.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.memory_button:
                pager.setCurrentItem(0);
                break;
            case R.id.title_text:
                pager.setCurrentItem(1);
                break;
            case R.id.settings_button:
                pager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    ZAndroidSystemDK.init(this);
                    A.f(this);
                    //A.b(this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wallpaperReceiver != null) {
            unregisterReceiver(wallpaperReceiver);
        }
    }
}
