package com.valentineapp.lovetogether.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.utils.Constant;

/**
 * Created by lequy on 3/14/2017.
 */

public class InputPassActivity extends BaseActivity implements View.OnClickListener {
    private ImageView bgImg;
    private View transView;
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
    private SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_input_pass;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        sharedPreferences = MyApplication.getSharedPreferences();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bgImg = (ImageView) findViewById(R.id.bg_img);
        transView = findViewById(R.id.trans_view);
        incorrectPassword = (TextView) findViewById(R.id.incorrect_password);
        deleteText = (TextView) findViewById(R.id.delete_text);
        mLinearLayout = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        number1 = (RelativeLayout) findViewById(R.id.no_1);
        number2 = (RelativeLayout) findViewById(R.id.no_2);
        number3 = (RelativeLayout) findViewById(R.id.no_3);
        number4 = (RelativeLayout) findViewById(R.id.no_4);
        number5 = (RelativeLayout) findViewById(R.id.no_5);
        number6 = (RelativeLayout) findViewById(R.id.no_6);
        number7 = (RelativeLayout) findViewById(R.id.no_7);
        number8 = (RelativeLayout) findViewById(R.id.no_8);
        number9 = (RelativeLayout) findViewById(R.id.no_9);
        number0 = (TextView) findViewById(R.id.no_0);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        bgImg.setImageResource(sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));

        transView.getLayoutParams().height = Constant.getStatusBarHeight(this);
        transView.requestLayout();

        currentPassword = sharedPreferences.getString("currentPass", "");
        is4Char = sharedPreferences.getBoolean("4char password", true);
        if (is4Char) {
            dotsQuantity = 4;
        } else {
            dotsQuantity = 6;
        }

        checkPassEmpty();

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
                    sendBroadcast(i);
                } else {
                    password = "";
                    drawPageSelectionIndicators(password.length());
                    checkPassEmpty();
                }
                break;
        }
    }

    private void checkPassEmpty() {
        if (password.equals("")) {
            deleteText.setText(this.getResources().getString(R.string.cancel));
        } else {
            deleteText.setText(this.getResources().getString(R.string.delete_text));
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
                startActivity(MainActivity.class);
                finish();
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
        int margin = Constant.convertDpIntoPixels(10, this);

        if (mLinearLayout != null) {
            mLinearLayout.removeAllViews();
        }

        mDots = new ImageView[dotsQuantity];

        //set image with orange circle if mDots[i] == mPosition
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new ImageView(this);
            if (i < passLength)
                mDots[i].setImageDrawable(this.getResources().getDrawable(R.mipmap.ic_pass));
            else
                mDots[i].setImageDrawable(this.getResources().getDrawable(R.mipmap.ic_pass_empty));

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
}
