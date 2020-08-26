package com.valentineapp.lovetogether.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.valentineapp.lovetogether.R;

import java.util.ArrayList;

/**
 * Created by lequy on 2/6/2017.
 */

public class ListMenuAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<String> listMenu;

    public ListMenuAdapter(Activity context, ArrayList<String> listMenu) {
        this.context = context;
        this.listMenu = listMenu;
    }

    @Override
    public int getCount() {
        return listMenu.size();
    }

    @Override
    public Object getItem(int position) {
        return listMenu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(R.layout.item_menu, parent, false);
        }

        TextView menuName = (TextView) convertView.findViewById(R.id.menu_name);
        menuName.setText(listMenu.get(position));

        return convertView;
    }
}
