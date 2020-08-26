package com.valentineapp.lovetogether.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.dialogs.ChangePassTypeDialog;
import com.valentineapp.lovetogether.utils.Constant;

/**
 * Created by lequy on 3/14/2017.
 */

public class ChangePassActivity extends BaseActivity implements View.OnClickListener {
    private View transView;
    private TextView titleText;
    private TextView incorrectPassword;
    private TextView clearText;
    private TextView confirmText;
    private TextView passType;
    private ImageView backgroundImage;
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
    private String tempNewPass = "";
    private String currentPassword;
    private SharedPreferences sharedPreferences;
    private boolean is4CharPre;
    private boolean is4Char;
    private int confirmType = 0;
    private Animation shake;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_change_pass;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        sharedPreferences = MyApplication.getSharedPreferences();
        transView = findViewById(R.id.trans_view);
        titleText = (TextView) findViewById(R.id.title_text);
        incorrectPassword = (TextView) findViewById(R.id.incorrect_password);
        clearText = (TextView) findViewById(R.id.clear_text);
        confirmText = (TextView) findViewById(R.id.confirm_text);
        passType = (TextView) findViewById(R.id.pass_type);
        backgroundImage = (ImageView) findViewById(R.id.bg_img);
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
        transView.getLayoutParams().height = Constant.getStatusBarHeight(this);
        transView.requestLayout();

        currentPassword = sharedPreferences.getString("currentPass", "");
        is4Char = sharedPreferences.getBoolean("4char password", true);
        is4CharPre = sharedPreferences.getBoolean("4char password", true);

        titleText.setText(getResources().getString(R.string.enter_new_password));
        confirmText.setText(getResources().getString(R.string.next));
        confirmType = 1;

        if (is4Char) {
            dotsQuantity = 4;
        } else {
            dotsQuantity = 6;
        }

        if (confirmType == 0) {
            drawPageSelectionIndicators(currentPassword.length(), password.length());
        } else {
            drawPageSelectionIndicators(dotsQuantity, password.length());
        }

        if (confirmType == 1) {
            passType.setVisibility(View.VISIBLE);
        } else {
            passType.setVisibility(View.GONE);
        }

        backgroundImage.setImageResource(sharedPreferences.getInt("bg_img", R.mipmap.wallpaper01));

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
        clearText.setOnClickListener(this);
        confirmText.setOnClickListener(this);
        passType.setOnClickListener(this);
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
            case R.id.pass_type:
                ChangePassTypeDialog dialog = new ChangePassTypeDialog(this, new ChangePassTypeDialog.ButtonListener() {
                    @Override
                    public void okClicked() {
                        password = "";
                        is4CharPre = sharedPreferences.getBoolean("4char password pre", true);
                        if (is4CharPre) {
                            dotsQuantity = 4;
                        } else {
                            dotsQuantity = 6;
                        }
                        drawPageSelectionIndicators(dotsQuantity, password.length());
                    }
                });
                dialog.show();
                break;
            case R.id.clear_text:
                password = "";
                if (confirmType == 0) {
                    drawPageSelectionIndicators(currentPassword.length(), password.length());
                } else {
                    drawPageSelectionIndicators(dotsQuantity, password.length());
                }
                break;
            case R.id.confirm_text:
                switch (confirmType) {
                    case 0:
                        if (password.equals(currentPassword)) {
                            titleText.setText(getResources().getString(R.string.enter_new_password));
                            confirmText.setText(getResources().getString(R.string.next));
                            password = "";
                            drawPageSelectionIndicators(dotsQuantity, password.length());
                            confirmType = 1;
                            passType.setVisibility(View.VISIBLE);
                        } else {
                            password = "";
                            drawPageSelectionIndicators(currentPassword.length(), password.length());
                            incorrectPassword.setVisibility(View.VISIBLE);
                            incorrectPassword.setText(getResources().getString(R.string.incorrect_pass));
                            mLinearLayout.startAnimation(shake);
                        }
                        break;
                    case 1:
                        if (password.length() == dotsQuantity) {
                            titleText.setText(getResources().getString(R.string.confirm_password));
                            confirmText.setText("OK");
                            tempNewPass = password;
                            password = "";
                            drawPageSelectionIndicators(dotsQuantity, password.length());
                            confirmType = 2;
                            passType.setVisibility(View.GONE);
                        } else {
                            password = "";
                            drawPageSelectionIndicators(dotsQuantity, password.length());
                            incorrectPassword.setVisibility(View.VISIBLE);
                            incorrectPassword.setText(getResources().getString(R.string.please_enter_correct));
                        }
                        break;
                    case 2:
                        if (password.equals(tempNewPass)) {
                            sharedPreferences.edit().putString("currentPass", password).apply();
                            sharedPreferences.edit().putBoolean("4char password", is4CharPre).apply();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            password = "";
                            drawPageSelectionIndicators(dotsQuantity, password.length());
                            incorrectPassword.setVisibility(View.VISIBLE);
                            incorrectPassword.setText(getResources().getString(R.string.incorrect_pass));
                            mLinearLayout.startAnimation(shake);
                        }
                        break;
                }
                break;
        }
    }

    private void inputPassword(String s) {
        if (incorrectPassword.getVisibility() == View.VISIBLE) {
            incorrectPassword.setVisibility(View.GONE);
        }
        if (confirmType == 0) {
            if (password.length() < currentPassword.length()) {
                password += s;
                drawPageSelectionIndicators(currentPassword.length(), password.length());
            }
        } else {
            if (password.length() < dotsQuantity) {
                password += s;
                drawPageSelectionIndicators(dotsQuantity, password.length());
            }
        }
    }

    private void drawPageSelectionIndicators(int dotsNum, int passLength) {
        int margin = Constant.convertDpIntoPixels(10, this);

        if (mLinearLayout != null) {
            mLinearLayout.removeAllViews();
        }

        mDots = new ImageView[dotsNum];

        //set image with orange circle if mDots[i] == mPosition
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new ImageView(this);
            if (i < passLength)
                mDots[i].setImageDrawable(getResources().getDrawable(R.mipmap.ic_pass));
            else
                mDots[i].setImageDrawable(getResources().getDrawable(R.mipmap.ic_pass_empty));

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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
