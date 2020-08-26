package com.valentineapp.lovetogether.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.valentineapp.lovetogether.R;

import java.util.ArrayList;

/**
 * Created by lequy on 2/6/2017.
 */

public class ListWallpaperAdapter extends RecyclerView.Adapter<ListWallpaperAdapter.RecyclerViewHolder> {
    private Activity context;
    private ArrayList<Integer> listWallpaper;
    private PositionClickListener listener;

    private int selectedPos = 0;

    public ListWallpaperAdapter(Activity context, ArrayList<Integer> listWallpaper, PositionClickListener listener) {
        this.context = context;
        this.listWallpaper = listWallpaper;
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = context.getLayoutInflater().inflate(R.layout.item_wallpaper, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Picasso.with(context).load(listWallpaper.get(position)).into(holder.wallpaperImg);

        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return listWallpaper.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  interface PositionClickListener {
        void itemClicked(int position);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView wallpaperImg;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            wallpaperImg = (ImageView) itemView.findViewById(R.id.wallpaper_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.itemClicked(getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }
}
