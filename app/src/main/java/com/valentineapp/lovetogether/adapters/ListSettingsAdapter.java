package com.valentineapp.lovetogether.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.utils.Constant;

import java.util.ArrayList;

/**
 * Created by lequy on 2/6/2017.
 */

public class ListSettingsAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<String> listSettings;
    private SharedPreferences sharedPreferences;

    public ListSettingsAdapter(Activity context, ArrayList<String> listSettings) {
        this.context = context;
        this.listSettings = listSettings;
        sharedPreferences = MyApplication.getSharedPreferences();
    }

    @Override
    public int getCount() {
        return listSettings.size();
    }

    @Override
    public Object getItem(int position) {
        return listSettings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.item_settings, parent, false);
        }

        TextView settingsName = (TextView) convertView.findViewById(R.id.settings_name);
        Switch switchButton = (Switch) convertView.findViewById(R.id.switch_button);
        settingsName.setText(listSettings.get(position));

        if (position == 3) {
            switchButton.setVisibility(View.GONE);
        } else {
            switchButton.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    if (sharedPreferences.getBoolean("noti", false)) {
                        switchButton.setChecked(true);
                    } else {
                        switchButton.setChecked(false);
                    }
                    break;
                case 1:
                    if (sharedPreferences.getBoolean("lockscreen", false)) {
                        switchButton.setChecked(true);
                    } else {
                        switchButton.setChecked(false);
                    }
                    break;
                case 2:
                    if (sharedPreferences.getBoolean("password", false)) {
                        switchButton.setChecked(true);
                    } else {
                        switchButton.setChecked(false);
                    }
                    break;
            }
        }

        /*switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(Constant.DISPLAY_NOTI);
                        if (isChecked) {
                            i.putExtra("command", 1);
                            context.sendBroadcast(i);
                            sharedPreferences.edit().putBoolean("noti", true).apply();
                        } else {
                            i.putExtra("command", 0);
                            context.sendBroadcast(i);
                            sharedPreferences.edit().putBoolean("noti", false).apply();
                        }
                        break;
                    case 1:
                        if (isChecked) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (!Settings.canDrawOverlays(context.getApplicationContext())) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + context.getPackageName()));
                                    context.startActivityForResult(intent, 1234);
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
                    *//*case 2:
                        if (isChecked) {
                            sharedPreferences.edit().putBoolean("password", true).apply();
                        } else {
                            sharedPreferences.edit().putBoolean("password", false).apply();
                        }
                        break;*//*
                }
            }
        });*/

        return convertView;
    }
}
