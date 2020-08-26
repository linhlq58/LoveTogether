package com.valentineapp.lovetogether.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.models.Memory;
import com.valentineapp.lovetogether.utils.AnastasTextView;
import com.valentineapp.lovetogether.utils.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lequy on 2/3/2017.
 */

public class ListMemoryAdapter extends RecyclerView.Adapter<ListMemoryAdapter.RecyclerViewHolder> {
    private Activity context;
    private ArrayList<Memory> listMemory;
    private SharedPreferences sharedPreferences;
    private Date memoryDate, startDate;
    private String startDateString;
    private long days;

    private int selectedPos = 0;
    private PositionClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
    private SimpleDateFormat dateToParse = new SimpleDateFormat("dd/MM/yyyy");

    public ListMemoryAdapter(Activity context, ArrayList<Memory> listMemory, PositionClickListener listener) {
        this.context = context;
        this.listMemory = listMemory;
        this.listener = listener;
        sharedPreferences = MyApplication.getSharedPreferences();
        startDateString = sharedPreferences.getString("start_date", "");
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = context.getLayoutInflater().inflate(R.layout.item_memory, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (listMemory.get(position).getImage64() != null) {
            holder.bgImg.setImageBitmap(Constant.getBase64Img(listMemory.get(position).getImage64()));
        }

        try {
            memoryDate = dateToParse.parse(listMemory.get(position).getTime());
            if (startDateString.equals("")) {
                days = 0;
            } else {
                startDate = dateToParse.parse(startDateString);
                days = Constant.daysBetween(startDate, memoryDate) + 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.dateText.setText(dateFormat.format(memoryDate));
        holder.memoryText.setText(listMemory.get(position).getMemoryName());
        holder.dayText.setText(context.getResources().getString(R.string.days) + " " + days);

        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return listMemory.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateStartDate() {
        startDateString = sharedPreferences.getString("start_date", "");
        notifyDataSetChanged();
    }

    public interface PositionClickListener {
        void itemClicked(int position);
        void itemLongClicked(int position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public ImageView bgImg;
        public TextView dateText;
        public TextView memoryText;
        public TextView dayText;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            bgImg = (ImageView) itemView.findViewById(R.id.bg_img);
            dateText = (TextView) itemView.findViewById(R.id.date_text);
            memoryText = (TextView) itemView.findViewById(R.id.memory_text);
            dayText = (TextView) itemView.findViewById(R.id.day_text);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);

            return false;
        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }
}
