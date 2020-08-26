package com.valentineapp.lovetogether.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.adapters.ListMenuAdapter;

import java.util.ArrayList;

/**
 * Created by lequy on 2/6/2017.
 */

public class MenuDialog extends Dialog {
    private Activity context;
    private ArrayList<String> listMenu;
    private PositionClicked listener;

    private ListView listView;
    private ListMenuAdapter adapter;

    public MenuDialog(Activity context, ArrayList<String> listMenu, PositionClicked listener) {
        super(context);
        this.context = context;
        this.listMenu = listMenu;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_menu);

        listView = (ListView) findViewById(R.id.list_menu);

        adapter = new ListMenuAdapter(context, listMenu);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.itemClicked(position);
                dismiss();
            }
        });
    }

    public interface PositionClicked {
        void itemClicked(int position);
    }
}
