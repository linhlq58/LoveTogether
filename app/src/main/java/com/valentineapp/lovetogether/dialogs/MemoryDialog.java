package com.valentineapp.lovetogether.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.utils.Constant;

/**
 * Created by lequy on 2/8/2017.
 */

public class MemoryDialog extends Dialog {
    private Activity context;
    private String image64;
    private String anniversary;
    private String date;

    private ImageView bgImg;
    private TextView anniversaryText;
    private TextView dateText;
    private Button shareButton;

    public MemoryDialog(Activity context, String image64, String anniversary, String date) {
        super(context);
        this.context = context;
        this.image64 = image64;
        this.anniversary = anniversary;
        this.date = date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_memory);

        bgImg = (ImageView) findViewById(R.id.bg_img);
        anniversaryText = (TextView) findViewById(R.id.anniversary_text);
        dateText = (TextView) findViewById(R.id.date_text);
        shareButton = (Button) findViewById(R.id.share_button);

        bgImg.setImageBitmap(Constant.getBase64Img(image64));
        anniversaryText.setText(anniversary);
        dateText.setText(date);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap screenCaptured = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                Constant.initShareIntent(context, "com.facebook.katana", screenCaptured);
            }
        });
    }
}
