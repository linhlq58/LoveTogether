package com.valentineapp.lovetogether.fragments;

import android.app.DatePickerDialog;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.activities.ChangeWallpaperActivity;
import com.valentineapp.lovetogether.dialogs.ChangeNameDialog;
import com.valentineapp.lovetogether.dialogs.MenuDialog;
import com.valentineapp.lovetogether.utils.Constant;
import com.valentineapp.lovetogether.utils.VoyageTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lequy on 2/3/2017.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout daysCountLayout;
    private LinearLayout maleLayout;
    private LinearLayout femaleLayout;
    private VoyageTextView daysText;
    private CircularImageView maleImg;
    private CircularImageView femaleImg;
    private VoyageTextView maleName;
    private VoyageTextView femaleName;
    private ArrayList<String> list1;
    private ArrayList<String> list2;
    private ArrayList<String> list3;
    private SharedPreferences sharedPreferences;
    private String maleImg64;
    private String femaleImg64;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date startDate, endDate;
    private long days;
    private WallpaperManager wallpaperManager;

    private final int PICK_IMAGE = 1;
    private int gender;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        sharedPreferences = MyApplication.getSharedPreferences();
        wallpaperManager = WallpaperManager.getInstance(getActivity());
        daysCountLayout = (RelativeLayout) rootView.findViewById(R.id.days_count_layout);
        maleLayout = (LinearLayout) rootView.findViewById(R.id.male_layout);
        femaleLayout = (LinearLayout) rootView.findViewById(R.id.female_layout);
        daysText = (VoyageTextView) rootView.findViewById(R.id.days_text);
        maleImg = (CircularImageView) rootView.findViewById(R.id.male_img);
        femaleImg = (CircularImageView) rootView.findViewById(R.id.female_img);
        maleName = (VoyageTextView) rootView.findViewById(R.id.male_name);
        femaleName = (VoyageTextView) rootView.findViewById(R.id.female_name);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        list1 = new ArrayList<>();
        list1.add(getActivity().getResources().getString(R.string.change_date));
        list1.add(getActivity().getResources().getString(R.string.change_wallpaper));
        list1.add(getActivity().getResources().getString(R.string.screen_capture));

        list2 = new ArrayList<>();
        list2.add(getActivity().getResources().getString(R.string.change_profile_picture));
        list2.add(getActivity().getResources().getString(R.string.change_nickname));

        list3 = new ArrayList<>();
        list3.add(getActivity().getResources().getString(R.string.save_sdcard));
        list3.add(getActivity().getResources().getString(R.string.set_wallpaper));
        list3.add(getActivity().getResources().getString(R.string.share_fb));

        endDate = new Date();
        String startDateString = sharedPreferences.getString("start_date", "");
        startDate = new Date();
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

        daysText.setText(days + "");
        if (sharedPreferences.getString("male_name", "").equals("")) {
            maleName.setText(getActivity().getResources().getString(R.string.nickname_1));
        } else {
            maleName.setText(sharedPreferences.getString("male_name", ""));
        }
        if (sharedPreferences.getString("female_name", "").equals("")) {
            femaleName.setText(getActivity().getResources().getString(R.string.nickname_2));
        } else {
            femaleName.setText(sharedPreferences.getString("female_name", ""));
        }

        maleImg64 = sharedPreferences.getString("male_img_64", "");
        femaleImg64 = sharedPreferences.getString("female_img_64", "");

        if (maleImg64.equals("")) {
            maleImg.setImageResource(R.mipmap.ic_launcher);
        } else {
            maleImg.setImageBitmap(Constant.getBase64Img(maleImg64));
        }

        if (femaleImg64.equals("")) {
            femaleImg.setImageResource(R.mipmap.ic_launcher);
        } else {
            femaleImg.setImageBitmap(Constant.getBase64Img(femaleImg64));
        }

        daysCountLayout.setOnClickListener(this);
        maleLayout.setOnClickListener(this);
        femaleLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.days_count_layout:
                final MenuDialog dialog1 = new MenuDialog(getActivity(), list1, new MenuDialog.PositionClicked() {
                    @Override
                    public void itemClicked(int position) {
                        switch (position) {
                            case 0:
                                final Calendar calendar = Calendar.getInstance();
                                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        String startDateString = Constant.formatTime(dayOfMonth) + "/" + Constant.formatTime(month + 1) + "/" + year;
                                        Date startDate = null;
                                        try {
                                            startDate = dateFormat.parse(startDateString);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Date endDate = new Date();
                                        long days = Constant.daysBetween(startDate, endDate) + 1;
                                        daysText.setText(String.valueOf(days));

                                        sharedPreferences.edit().putString("start_date", startDateString).apply();
                                        sharedPreferences.edit().putLong("days", days).apply();

                                        Intent i = new Intent(Constant.CHANGE_NOTI);
                                        i.putExtra("command", "date");
                                        getActivity().sendBroadcast(i);
                                    }
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                                datePicker.show();
                                break;
                            case 1:
                                startActivity(ChangeWallpaperActivity.class);
                                break;
                            case 2:
                                MenuDialog dialog4 = new MenuDialog(getActivity(), list3, new MenuDialog.PositionClicked() {
                                    @Override
                                    public void itemClicked(int position) {
                                        View v1 = getActivity().getWindow().getDecorView().getRootView();
                                        v1.setDrawingCacheEnabled(true);
                                        Bitmap screenCaptured = Bitmap.createBitmap(v1.getDrawingCache());
                                        v1.setDrawingCacheEnabled(false);

                                        switch (position) {
                                            case 0:
                                                String IMAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/LoveTogether";

                                                File folder = new File(IMAGE_PATH);

                                                if (!folder.exists()) {
                                                    folder.mkdir();
                                                }

                                                String s = System.currentTimeMillis() + ".jpg";

                                                File file = new File(folder.getAbsolutePath(), s);

                                                try {
                                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                                    screenCaptured.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();

                                                    screenCaptured.recycle();

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (Exception exception) {
                                                    exception.printStackTrace();
                                                }

                                                Constant.addImageToGallery(IMAGE_PATH + "/" + s, getActivity());
                                                break;
                                            case 1:
                                                try {
                                                    wallpaperManager.setBitmap(screenCaptured);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case 2:
                                                Constant.initShareIntent(getActivity(), "com.facebook.katana", screenCaptured);
                                                break;
                                        }
                                    }
                                });
                                dialog4.show();
                                break;
                        }
                    }
                });
                dialog1.show();
                break;
            case R.id.male_layout:
                MenuDialog dialog2 = new MenuDialog(getActivity(), list2, new MenuDialog.PositionClicked() {
                    @Override
                    public void itemClicked(int position) {
                        switch (position) {
                            case 0:
                                gender = 1;
                                pickImage();
                                break;
                            case 1:
                                ChangeNameDialog nameDialog1 = new ChangeNameDialog(getActivity(), sharedPreferences.getString("male_name", ""), new ChangeNameDialog.OkClickedListener() {
                                    @Override
                                    public void okClicked(String name) {
                                        maleName.setText(name);
                                        sharedPreferences.edit().putString("male_name", name).apply();

                                        Intent i = new Intent(Constant.CHANGE_NOTI);
                                        i.putExtra("command", "male_name");
                                        getActivity().sendBroadcast(i);
                                    }
                                });
                                nameDialog1.show();
                                break;
                        }
                    }
                });
                dialog2.show();
                break;
            case R.id.female_layout:
                MenuDialog dialog3 = new MenuDialog(getActivity(), list2, new MenuDialog.PositionClicked() {
                    @Override
                    public void itemClicked(int position) {
                        switch (position) {
                            case 0:
                                gender = 2;
                                pickImage();
                                break;
                            case 1:
                                ChangeNameDialog nameDialog2 = new ChangeNameDialog(getActivity(), sharedPreferences.getString("female_name", ""), new ChangeNameDialog.OkClickedListener() {
                                    @Override
                                    public void okClicked(String name) {
                                        femaleName.setText(name);
                                        sharedPreferences.edit().putString("female_name", name).apply();

                                        Intent i = new Intent(Constant.CHANGE_NOTI);
                                        i.putExtra("command", "female_name");
                                        getActivity().sendBroadcast(i);
                                    }
                                });
                                nameDialog2.show();
                                break;
                        }
                    }
                });
                dialog3.show();
                break;
        }
    }

    private void pickImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap selectedImage = Constant.decodeUri(getActivity(), resultUri);
                    String encodedImage = Constant.encodeImage(selectedImage);

                    if (gender == 1) {
                        maleImg.setImageBitmap(selectedImage);
                        sharedPreferences.edit().putString("male_img_64", encodedImage).apply();

                        Intent i = new Intent(Constant.CHANGE_NOTI);
                        i.putExtra("command", "male_img");
                        getActivity().sendBroadcast(i);
                    } else {
                        femaleImg.setImageBitmap(selectedImage);
                        sharedPreferences.edit().putString("female_img_64", encodedImage).apply();

                        Intent i = new Intent(Constant.CHANGE_NOTI);
                        i.putExtra("command", "female_img");
                        getActivity().sendBroadcast(i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(getContext(), this);
            }
        }
    }
}
