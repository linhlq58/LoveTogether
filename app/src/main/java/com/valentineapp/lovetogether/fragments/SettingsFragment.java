package com.valentineapp.lovetogether.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.activities.ChangePassActivity;
import com.valentineapp.lovetogether.activities.ChangeWallpaperActivity;
import com.valentineapp.lovetogether.adapters.ListSettingsAdapter;
import com.valentineapp.lovetogether.services.MyService;
import com.valentineapp.lovetogether.utils.Constant;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by lequy on 2/3/2017.
 */

public class SettingsFragment extends BaseFragment {
    private ListView listSettingsView;
    private ListSettingsAdapter listSettingsAdapter;
    private ArrayList<String> listSettings;
    private SharedPreferences sharedPreferences;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        sharedPreferences = MyApplication.getSharedPreferences();
        listSettingsView = (ListView) rootView.findViewById(R.id.list_settings);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listSettings = new ArrayList<>();
        listSettings.add(getActivity().getResources().getString(R.string.notifications));
        listSettings.add(getActivity().getResources().getString(R.string.lock_screen));
        listSettings.add(getActivity().getResources().getString(R.string.password));
        listSettings.add(getActivity().getResources().getString(R.string.change_wallpaper));

        listSettingsAdapter = new ListSettingsAdapter(getActivity(), listSettings);

        listSettingsView.setAdapter(listSettingsAdapter);

        listSettingsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Switch switch1 = (Switch) listSettingsView.getChildAt(0).findViewById(R.id.switch_button);
                Switch switch2 = (Switch) listSettingsView.getChildAt(1).findViewById(R.id.switch_button);
                Switch switch3 = (Switch) listSettingsView.getChildAt(2).findViewById(R.id.switch_button);

                switch (position) {
                    case 0:
                        switch1.performClick();

                        Intent i = new Intent(Constant.DISPLAY_NOTI);
                        if (switch1.isChecked()) {
                            i.putExtra("command", 1);
                            getActivity().sendBroadcast(i);
                            sharedPreferences.edit().putBoolean("noti", true).apply();
                        } else {
                            i.putExtra("command", 0);
                            getActivity().sendBroadcast(i);
                            sharedPreferences.edit().putBoolean("noti", false).apply();
                        }
                        break;
                    case 1:
                        switch2.performClick();
                        if (switch2.isChecked()) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (!Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + getActivity().getPackageName()));
                                    getActivity().startActivityForResult(intent, 1234);
                                } else {
                                    sharedPreferences.edit().putBoolean("lockscreen", true).apply();
                                }
                            } else {
                                sharedPreferences.edit().putBoolean("lockscreen", true).apply();
                            }
                        } else {
                            sharedPreferences.edit().putBoolean("lockscreen", false).apply();
                        }
                        break;
                    case 2:
                        switch3.performClick();
                        if (switch3.isChecked()) {
                            Intent passIntent = new Intent(getActivity(), ChangePassActivity.class);
                            startActivityForResult(passIntent, 1);
                        } else {
                            sharedPreferences.edit().putBoolean("password", false).apply();
                        }
                        break;
                    case 3:
                        startActivity(ChangeWallpaperActivity.class);
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Switch switch3 = (Switch) listSettingsView.getChildAt(2).findViewById(R.id.switch_button);
            if (resultCode == RESULT_OK) {
                switch3.setChecked(true);
                sharedPreferences.edit().putBoolean("password", true).apply();
            } else if (resultCode == RESULT_CANCELED) {
                switch3.setChecked(false);
            }
        }
    }
}
