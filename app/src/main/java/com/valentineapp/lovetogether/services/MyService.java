package com.valentineapp.lovetogether.services;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.activities.LockActivity;
import com.valentineapp.lovetogether.activities.MainActivity;
import com.valentineapp.lovetogether.adapters.LockPagerAdapter;
import com.valentineapp.lovetogether.utils.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lequy on 2/7/2017.
 */

public class MyService extends Service {
    private static int NOTIFY_ID = 1;

    private BroadcastReceiver notiReceiver, lockReceiver, createViewReceiver, changeDateReceiver,
            timeTickReceiver, timeChangedReceiver, unlockReceiver, haveCallReceiver;
    private Notification noti;
    private RemoteViews contentView;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    private ViewGroup lockView;
    private SharedPreferences sharedPreferences;
    private String maleImg64;
    private String femaleImg64;
    private String maleName;
    private String femaleName;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private ImageView bgImg;
    private ViewPager pager;
    private LockPagerAdapter adapter;
    private boolean lockIsAdded = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = MyApplication.getSharedPreferences();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(MyService.this);
        lockView = (ViewGroup) inflater.inflate(R.layout.lock_view, null);

        KeyguardManager.KeyguardLock key;
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        //This is deprecated, but it is a simple way to disable the lockscreen in code
        key = km.newKeyguardLock("IN");
        key.disableKeyguard();

        //Start listening for the Screen On, Screen Off, and Boot completed actions
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        //Set up a receiver to listen for the Intents in this Service
        lockReceiver = new LockScreenReceiver();
        registerReceiver(lockReceiver, filter);

        notiReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int command = extras.getInt("command");
                    if (command == 1) {
                        createNotification();
                        startForeground(NOTIFY_ID, noti);
                    } else {
                        stopForeground(true);
                    }
                }
            }
        };
        registerReceiver(notiReceiver, new IntentFilter(Constant.DISPLAY_NOTI));

        createViewReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                createLockView();
            }
        };
        registerReceiver(createViewReceiver, new IntentFilter(Constant.CREATE_LOCK_VIEW));

        changeDateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (sharedPreferences.getBoolean("noti", false) && contentView != null) {
                    Bundle extras = intent.getExtras();
                    String command = extras.getString("command");
                    if (command.equals("date")) {
                        contentView.setTextViewText(R.id.days_text, calculateDays() + " " + getResources().getString(R.string.days));
                        startForeground(NOTIFY_ID, noti);
                    } else if (command.equals("wallpaper")) {
                        contentView.setImageViewResource(R.id.bg_img, sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));
                        startForeground(NOTIFY_ID, noti);
                    } else if (command.equals("male_name")) {
                        maleName = sharedPreferences.getString("male_name", "");
                        contentView.setTextViewText(R.id.male_name, maleName);
                        startForeground(NOTIFY_ID, noti);
                    } else if (command.equals("female_name")) {
                        femaleName = sharedPreferences.getString("female_name", "");
                        contentView.setTextViewText(R.id.female_name, femaleName);
                        startForeground(NOTIFY_ID, noti);
                    } else if (command.equals("male_img")) {
                        maleImg64 = sharedPreferences.getString("male_img_64", "");

                        Bitmap bm1 = null;
                        if (maleImg64.equals("")) {
                            bm1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        } else {
                            bm1 = Constant.getBase64Img(maleImg64);
                        }

                        contentView.setImageViewBitmap(R.id.male_img, Constant.getCircleBitmap(bm1));
                        startForeground(NOTIFY_ID, noti);
                    } else if (command.equals("female_img")) {
                        femaleImg64 = sharedPreferences.getString("female_img_64", "");

                        Bitmap bm2 = null;
                        if (femaleImg64.equals("")) {
                            bm2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        } else {
                            bm2 = Constant.getBase64Img(femaleImg64);
                        }

                        contentView.setImageViewBitmap(R.id.female_img, Constant.getCircleBitmap(bm2));
                        startForeground(NOTIFY_ID, noti);
                    }
                }
            }
        };
        registerReceiver(changeDateReceiver, new IntentFilter(Constant.CHANGE_NOTI));

        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    Date date = new Date();
                    if ((date.getHours() == 0) && (date.getMinutes() == 0)) {
                        if (sharedPreferences.getBoolean("noti", false) && contentView != null) {
                            contentView.setTextViewText(R.id.days_text, calculateDays() + " " + getResources().getString(R.string.days));
                            startForeground(NOTIFY_ID, noti);
                        }
                    }
                }
            }
        };
        registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

        IntentFilter timeChangedFilter = new IntentFilter();
        timeChangedFilter.addAction(Intent.ACTION_TIME_CHANGED);
        timeChangedFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        timeChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (sharedPreferences.getBoolean("noti", false) && contentView != null) {
                    contentView.setTextViewText(R.id.days_text, calculateDays() + " " + getResources().getString(R.string.days));
                    startForeground(NOTIFY_ID, noti);
                }
            }
        };
        registerReceiver(timeChangedReceiver, timeChangedFilter);

        unlockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                unlockScreen();
            }
        };
        registerReceiver(unlockReceiver, new IntentFilter("unlock"));

        haveCallReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                String command = extras.getString("command");
                if (command.equals("remove")) {
                    if (lockIsAdded) {
                        Intent i = new Intent(Constant.FINISH_ACTIVITY);
                        sendBroadcast(i);
                        mWindowManager.removeView(lockView);
                        lockIsAdded = false;
                    }
                } else if (command.equals("add")) {
                    if (sharedPreferences.getBoolean("lockscreen", false)) {
                        if (!lockIsAdded) {
                            Intent i = new Intent(context, LockActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(i);

                            createLockView();
                        }
                    }
                }
            }
        };
        registerReceiver(haveCallReceiver, new IntentFilter("have_a_call"));
    }

    private long calculateDays() {
        long days = 0;
        Date endDate = new Date();
        String startDateString = sharedPreferences.getString("start_date", "");
        Date startDate;
        try {
            if (startDateString.equals("")) {
                days = 0;
            } else {
                startDate = dateFormat.parse(startDateString);
                days = Constant.daysBetween(startDate, endDate) + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    private void createNotification() {
        maleImg64 = sharedPreferences.getString("male_img_64", "");
        femaleImg64 = sharedPreferences.getString("female_img_64", "");
        maleName = sharedPreferences.getString("male_name", "");
        femaleName = sharedPreferences.getString("female_name", "");

        Intent notiIntent = new Intent(this, MainActivity.class);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bm1 = null;
        Bitmap bm2 = null;

        if (maleImg64.equals("")) {
            bm1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        } else {
            bm1 = Constant.getBase64Img(maleImg64);
        }

        if (femaleImg64.equals("")) {
            bm2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        } else {
            bm2 = Constant.getBase64Img(femaleImg64);
        }

        contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.bg_img, sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));
        contentView.setImageViewBitmap(R.id.male_img, Constant.getCircleBitmap(bm1));
        contentView.setImageViewBitmap(R.id.female_img, Constant.getCircleBitmap(bm2));
        contentView.setTextViewText(R.id.male_name, maleName);
        contentView.setTextViewText(R.id.female_name, femaleName);
        contentView.setTextViewText(R.id.days_text, calculateDays() + " " + getResources().getString(R.string.days));

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendInt)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setContentTitle("Love")
                .setContent(contentView);

        noti = builder.build();
    }

    private void createLockView() {
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSLUCENT);

        bgImg = (ImageView) lockView.findViewById(R.id.bg_img);
        pager = (ViewPager) lockView.findViewById(R.id.pager);

        bgImg.setImageResource(sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));

        if (adapter == null) {
            adapter = new LockPagerAdapter(this);
        }

        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        pager.setOffscreenPageLimit(2);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!sharedPreferences.getBoolean("password", false)) {
                    if (position == 0) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                unlockScreen();
                            }
                        }, 200);
                    }
                } else {
                    adapter.clearPass();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable myIcon = getResources().getDrawable(R.mipmap.ic_bubble_heart);
                ParticleSystem particleSystem = new ParticleSystem(lockView, 20, myIcon, 10000);
                particleSystem.setSpeedModuleAndAngleRange(0.02f, 0.08f, 90,120);
                //particleSystem.setAcceleration(0.00005f, 90);
                particleSystem.emitWithGravity(lockView.findViewById(R.id.trans_view), Gravity.BOTTOM, 2);
            }
        }, 2000);*/

        if (!lockIsAdded) {
            mWindowManager.addView(lockView, mLayoutParams);
            lockIsAdded = true;
        }
    }

    public void unlockScreen() {
        //android.os.Process.killProcess(android.os.Process.myPid());
        Intent i = new Intent(Constant.FINISH_ACTIVITY);
        sendBroadcast(i);

        if (lockIsAdded) {
            mWindowManager.removeView(lockView);
            lockIsAdded = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notiReceiver != null) {
            unregisterReceiver(notiReceiver);
        }
        if (lockReceiver != null) {
            unregisterReceiver(lockReceiver);
        }
        if (createViewReceiver != null) {
            unregisterReceiver(createViewReceiver);
        }
        if (changeDateReceiver != null) {
            unregisterReceiver(changeDateReceiver);
        }
        if (timeTickReceiver != null) {
            unregisterReceiver(timeTickReceiver);
        }
        if (timeChangedReceiver != null) {
            unregisterReceiver(timeChangedReceiver);
        }
        if (unlockReceiver != null) {
            unregisterReceiver(unlockReceiver);
        }
        if (haveCallReceiver != null) {
            unregisterReceiver(haveCallReceiver);
        }
    }
}
