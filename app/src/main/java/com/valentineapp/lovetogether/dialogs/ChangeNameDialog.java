package com.valentineapp.lovetogether.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.valentineapp.lovetogether.R;

/**
 * Created by lequy on 2/6/2017.
 */

public class ChangeNameDialog extends Dialog {
    private Activity context;
    private String currentName;
    private OkClickedListener listener;

    private Button okButton;
    private Button cancelButton;
    private EditText nameEdit;

    public ChangeNameDialog(Activity context, String currentName, OkClickedListener listener) {
        super(context);
        this.context = context;
        this.currentName = currentName;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_change_nickname);

        okButton = (Button) findViewById(R.id.ok_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        nameEdit = (EditText) findViewById(R.id.name_edit);

        nameEdit.setText(currentName);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.okClicked(nameEdit.getText().toString());
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OkClickedListener {
        void okClicked(String name);
    }
}
