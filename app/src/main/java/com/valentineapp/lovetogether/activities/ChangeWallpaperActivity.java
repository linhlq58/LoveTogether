package com.valentineapp.lovetogether.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.adapters.ListWallpaperAdapter;
import com.valentineapp.lovetogether.utils.Constant;
import com.valentineapp.lovetogether.utils.GridAutofitLayoutManager;

import java.util.ArrayList;

/**
 * Created by lequy on 2/6/2017.
 */

public class ChangeWallpaperActivity extends BaseActivity {
    private View transView;
    private ImageView bgImg;
    private RecyclerView listWallpaperView;
    private ListWallpaperAdapter listWallpaperAdapter;
    private ArrayList<Integer> listWallpaper;
    private SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_change_wallpaper;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        sharedPreferences = MyApplication.getSharedPreferences();
        transView = findViewById(R.id.trans_view);
        bgImg = (ImageView) findViewById(R.id.bg_img);
        listWallpaperView = (RecyclerView) findViewById(R.id.list_wallpaper);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        bgImg.setImageResource(sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));

        transView.getLayoutParams().height = Constant.getStatusBarHeight(this);
        transView.requestLayout();

        listWallpaper = new ArrayList<>();
        listWallpaper.add(R.mipmap.wallpaper01);
        listWallpaper.add(R.mipmap.wallpaper02);
        listWallpaper.add(R.mipmap.wallpaper03);
        listWallpaper.add(R.mipmap.wallpaper04);
        listWallpaper.add(R.mipmap.wallpaper05);
        listWallpaper.add(R.mipmap.wallpaper06);
        listWallpaper.add(R.mipmap.wallpaper07);
        listWallpaper.add(R.mipmap.wallpaper08);
        listWallpaper.add(R.mipmap.wallpaper09);
        listWallpaper.add(R.mipmap.wallpaper10);
        listWallpaper.add(R.mipmap.wallpaper11);
        listWallpaper.add(R.mipmap.wallpaper12);
        listWallpaper.add(R.mipmap.wallpaper13);
        listWallpaper.add(R.mipmap.wallpaper14);
        listWallpaper.add(R.mipmap.wallpaper15);
        listWallpaper.add(R.mipmap.wallpaper16);
        listWallpaper.add(R.mipmap.wallpaper17);
        listWallpaper.add(R.mipmap.wallpaper18);
        listWallpaper.add(R.mipmap.wallpaper19);
        listWallpaper.add(R.mipmap.wallpaper20);
        listWallpaper.add(R.mipmap.wallpaper21);
        listWallpaper.add(R.mipmap.wallpaper22);
        listWallpaper.add(R.mipmap.wallpaper23);
        listWallpaper.add(R.mipmap.wallpaper24);

        listWallpaperAdapter = new ListWallpaperAdapter(this, listWallpaper, new ListWallpaperAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                sharedPreferences.edit().putInt("bg_img", listWallpaper.get(position)).apply();
                Intent i1 = new Intent(Constant.CHANGE_WALLPAPER);
                sendBroadcast(i1);
                Intent i2 = new Intent(Constant.CHANGE_NOTI);
                i2.putExtra("command", "wallpaper");
                sendBroadcast(i2);
                finish();
            }
        });

        listWallpaperView.setLayoutManager(new GridAutofitLayoutManager(this, Constant.convertDpIntoPixels(110, this)));
        listWallpaperView.setAdapter(listWallpaperAdapter);
    }
}
