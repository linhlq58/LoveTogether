package com.valentineapp.lovetogether.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.utils.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lequy on 2/8/2017.
 */

public class LockPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private static int NUM_ITEMS = 2;
    private Context context;
    private SharedPreferences sharedPreferences;

    private RelativeLayout unlockLayout;
    private TextView incorrectPassword;
    private TextView deleteText;
    private LinearLayout mLinearLayout;
    private ImageView[] mDots;
    private int dotsQuantity;
    private RelativeLayout number1;
    private RelativeLayout number2;
    private RelativeLayout number3;
    private RelativeLayout number4;
    private RelativeLayout number5;
    private RelativeLayout number6;
    private RelativeLayout number7;
    private RelativeLayout number8;
    private RelativeLayout number9;
    private TextView number0;
    private String password = "";
    private String currentPassword;
    private boolean is4Char;
    private Animation shake;
    private Vibrator vibrator;

    private String maleImg64;
    private String femaleImg64;
    private TextView timeText;
    private TextView dateText;
    private TextView daysText;
    private CircularImageView maleImg;
    private CircularImageView femaleImg;
    private ShimmerTextView slideText;
    private Shimmer shimmer;
    private Typeface myFont;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd");
    private SimpleDateFormat clockFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat dateToParse = new SimpleDateFormat("dd/MM/yyyy");
    private long days;
    private BroadcastReceiver timeTickReceiver;

    public LockPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        sharedPreferences = MyApplication.getSharedPreferences();
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewGroup unlockLayout = (ViewGroup) inflater.inflate(R.layout.fragment_unlock, container, false);
        ViewGroup lockLayout = (ViewGroup) inflater.inflate(R.layout.fragment_lock, container, false);
        View viewArr[] = {unlockLayout, lockLayout};
        if (position == 0) {
            handleUnlockView(unlockLayout);
        } else if (position == 1) {
            handleLockView(lockLayout);
        }
        container.addView(viewArr[position]);
        return viewArr[position];
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void handleUnlockView(ViewGroup view) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        unlockLayout = (RelativeLayout) view.findViewById(R.id.unlock_layout);
        incorrectPassword = (TextView) view.findViewById(R.id.incorrect_password);
        deleteText = (TextView) view.findViewById(R.id.delete_text);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        number1 = (RelativeLayout) view.findViewById(R.id.no_1);
        number2 = (RelativeLayout) view.findViewById(R.id.no_2);
        number3 = (RelativeLayout) view.findViewById(R.id.no_3);
        number4 = (RelativeLayout) view.findViewById(R.id.no_4);
        number5 = (RelativeLayout) view.findViewById(R.id.no_5);
        number6 = (RelativeLayout) view.findViewById(R.id.no_6);
        number7 = (RelativeLayout) view.findViewById(R.id.no_7);
        number8 = (RelativeLayout) view.findViewById(R.id.no_8);
        number9 = (RelativeLayout) view.findViewById(R.id.no_9);
        number0 = (TextView) view.findViewById(R.id.no_0);
        shake = AnimationUtils.loadAnimation(context, R.anim.shake);

        currentPassword = sharedPreferences.getString("currentPass", "");
        is4Char = sharedPreferences.getBoolean("4char password", true);
        if (is4Char) {
            dotsQuantity = 4;
        } else {
            dotsQuantity = 6;
        }

        checkPassEmpty();

        if (!sharedPreferences.getBoolean("password", false)) {
            unlockLayout.setVisibility(View.GONE);
        }

        drawPageSelectionIndicators(password.length());

        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);
        number0.setOnClickListener(this);
        deleteText.setOnClickListener(this);
    }

    private void handleLockView(ViewGroup view) {
        timeText = (TextView) view.findViewById(R.id.time_text);
        dateText = (TextView) view.findViewById(R.id.date_text);
        daysText = (TextView) view.findViewById(R.id.days_text);
        maleImg = (CircularImageView) view.findViewById(R.id.male_img);
        femaleImg = (CircularImageView) view.findViewById(R.id.female_img);
        slideText = (ShimmerTextView) view.findViewById(R.id.slide_text);
        myFont = Constant.setRobotoFont(context);

        maleImg64 = sharedPreferences.getString("male_img_64", "");
        femaleImg64 = sharedPreferences.getString("female_img_64", "");

        slideText.setTypeface(myFont);
        shimmer = new Shimmer();
        shimmer.start(slideText);

        Date endDate = new Date();
        String startDateString = sharedPreferences.getString("start_date", "");
        Date startDate = new Date();
        try {
            if (startDateString.equals("")) {
                days = 0;
            } else {
                startDate = dateToParse.parse(startDateString);
                days = Constant.daysBetween(startDate, endDate) + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        daysText.setText(days + " " + context.getResources().getString(R.string.days));
        timeText.setText(clockFormat.format(new Date()));
        dateText.setText(dateFormat.format(new Date()));

        maleImg.setImageBitmap(Constant.getBase64Img(maleImg64));
        femaleImg.setImageBitmap(Constant.getBase64Img(femaleImg64));

        timeTickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    timeText.setText(clockFormat.format(new Date()));
                    dateText.setText(dateFormat.format(new Date()));
                }
            }
        };
        context.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no_1:
                inputPassword("1");
                break;
            case R.id.no_2:
                inputPassword("2");
                break;
            case R.id.no_3:
                inputPassword("3");
                break;
            case R.id.no_4:
                inputPassword("4");
                break;
            case R.id.no_5:
                inputPassword("5");
                break;
            case R.id.no_6:
                inputPassword("6");
                break;
            case R.id.no_7:
                inputPassword("7");
                break;
            case R.id.no_8:
                inputPassword("8");
                break;
            case R.id.no_9:
                inputPassword("9");
                break;
            case R.id.no_0:
                inputPassword("0");
                break;
            case R.id.delete_text:
                if (password.equals("")) {
                    Intent i = new Intent("go_to_home");
                    context.sendBroadcast(i);
                } else {
                    password = "";
                    drawPageSelectionIndicators(password.length());
                    checkPassEmpty();
                }
                break;
        }
    }

    // Begin unlock view function
    private void checkPassEmpty() {
        if (password.equals("")) {
            deleteText.setText(context.getResources().getString(R.string.cancel));
        } else {
            deleteText.setText(context.getResources().getString(R.string.delete_text));
        }
    }

    private void inputPassword(String s) {
        if (incorrectPassword.getVisibility() == View.VISIBLE) {
            incorrectPassword.setVisibility(View.GONE);
        }
        if (password.length() < dotsQuantity) {
            password += s;
            drawPageSelectionIndicators(password.length());
        }
        if (password.length() == dotsQuantity) {
            if (password.equals(currentPassword)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent("unlock");
                        context.sendBroadcast(i);
                    }
                }, 200);
            } else {
                password = "";
                drawPageSelectionIndicators(password.length());
                incorrectPassword.setVisibility(View.VISIBLE);
                mLinearLayout.startAnimation(shake);
            }
        }
        vibrator.vibrate(100);

        checkPassEmpty();
    }

    public void clearPass() {
        password = "";
        drawPageSelectionIndicators(password.length());
        checkPassEmpty();
        incorrectPassword.setVisibility(View.GONE);
    }

    private void drawPageSelectionIndicators(int passLength) {
        int margin = Constant.convertDpIntoPixels(10, context);

        if (mLinearLayout != null) {
            mLinearLayout.removeAllViews();
        }

        mDots = new ImageView[dotsQuantity];

        //set image with orange circle if mDots[i] == mPosition
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new ImageView(context);
            if (i < passLength)
                mDots[i].setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pass));
            else
                mDots[i].setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_pass_empty));

            //set layout for circle indicators
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            mDots[i].setPadding(margin, 0, margin, 0);
            mLinearLayout.addView(mDots[i], params);
        }
    }
    // End unlock view function
}
