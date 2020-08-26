package com.valentineapp.lovetogether.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.utils.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lequy on 2/7/2017.
 */

public class AddMemoryDialog extends Dialog implements View.OnClickListener {
    private Activity context;
    private OkClickListener listener;

    private EditText editAnniversary;
    private RelativeLayout dateLayout;
    private RelativeLayout photoLayout;
    private TextView dateText;
    private ImageView selectedImageView;
    private Button okButton;
    private Button cancelButton;
    private Calendar calendar;
    private Date startDate, endDate;
    private long days;
    private String image64 = "";
    private String pickedDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SharedPreferences sharedPreferences;

    public AddMemoryDialog(Activity context, OkClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        sharedPreferences = MyApplication.getSharedPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_add_memory);

        editAnniversary = (EditText) findViewById(R.id.edit_anniversary);
        dateLayout = (RelativeLayout) findViewById(R.id.date_layout);
        photoLayout = (RelativeLayout) findViewById(R.id.photo_layout);
        dateText = (TextView) findViewById(R.id.date_text);
        selectedImageView = (ImageView) findViewById(R.id.selected_img);
        okButton = (Button) findViewById(R.id.ok_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        calendar = Calendar.getInstance();
        pickedDate = dateFormat.format(calendar.getTime());
        dateText.setText(pickedDate);

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

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        photoLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                if (image64.equals("")) {
                    Toast.makeText(context, "Please select photo", Toast.LENGTH_SHORT).show();
                } else {
                    if (editAnniversary.getText().toString().equals("")) {
                        Toast.makeText(context, "Please enter anniversary", Toast.LENGTH_SHORT).show();
                    } else {
                        listener.okClicked(pickedDate, editAnniversary.getText().toString(), days, image64);
                        dismiss();
                    }
                }
                break;
            case R.id.cancel_button:
                dismiss();
                break;
            case R.id.date_layout:
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        pickedDate = Constant.formatTime(dayOfMonth) + "/" + Constant.formatTime(month + 1) + "/" + year;
                        dateText.setText(pickedDate);

                        try {
                            endDate = dateFormat.parse(pickedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        days = Constant.daysBetween(startDate, endDate) + 1;
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePicker.show();
                break;
            case R.id.photo_layout:
                listener.photoClicked();
                break;
        }
    }

    public interface OkClickListener {
        void okClicked(String date, String anniversary, long days, String image64);
        void photoClicked();
    }

    public void setImage(Bitmap bm) {
        image64 = Constant.encodeImage(bm);
        selectedImageView.setVisibility(View.VISIBLE);
        selectedImageView.setImageBitmap(bm);
    }
}
