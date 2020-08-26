package com.valentineapp.lovetogether.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.valentineapp.lovetogether.MyApplication;
import com.valentineapp.lovetogether.R;
import com.valentineapp.lovetogether.adapters.ListMemoryAdapter;
import com.valentineapp.lovetogether.database.DatabaseHelper;
import com.valentineapp.lovetogether.dialogs.AddMemoryDialog;
import com.valentineapp.lovetogether.dialogs.MemoryDialog;
import com.valentineapp.lovetogether.dialogs.MenuDialog;
import com.valentineapp.lovetogether.models.Memory;
import com.valentineapp.lovetogether.utils.Constant;
import com.valentineapp.lovetogether.utils.SimpleDividerItemDecoration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lequy on 2/3/2017.
 */

public class MemoryFragment extends BaseFragment implements View.OnClickListener {
    private DatabaseHelper db;
    private ImageView addMemory;
    private RecyclerView listMemoryView;
    private ListMemoryAdapter listMemoryAdapter;
    private ArrayList<Memory> listMemory;
    private AddMemoryDialog dialog;
    private BroadcastReceiver changeDateReceiver;

    private final int PICK_IMAGE = 0;

    public static MemoryFragment newInstance() {

        Bundle args = new Bundle();

        MemoryFragment fragment = new MemoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_memory;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState, View rootView) {
        db = MyApplication.getDb();
        addMemory = (ImageView) rootView.findViewById(R.id.add_memory);
        listMemoryView = (RecyclerView) rootView.findViewById(R.id.list_memory);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        listMemory = db.getAllMemory();

        listMemoryAdapter = new ListMemoryAdapter(getActivity(), listMemory, new ListMemoryAdapter.PositionClickListener() {
            @Override
            public void itemClicked(int position) {
                Memory memory = listMemory.get(position);

                MemoryDialog memoryDialog = new MemoryDialog(getActivity(), memory.getImage64(), memory.getMemoryName(), memory.getTime());
                memoryDialog.show();
            }

            @Override
            public void itemLongClicked(final int position) {
                ArrayList<String> listMenu = new ArrayList<>();
                listMenu.add(getActivity().getResources().getString(R.string.delete));

                MenuDialog dialog = new MenuDialog(getActivity(), listMenu, new MenuDialog.PositionClicked() {
                    @Override
                    public void itemClicked(int menuPosition) {
                        db.deleteMemory(listMemory.get(position).getId());

                        listMemory.remove(position);
                        listMemoryAdapter.notifyItemRemoved(position);
                        listMemoryAdapter.notifyItemRangeChanged(position, listMemory.size());

                        addMemory.setVisibility(View.VISIBLE);
                    }
                });
                dialog.show();
            }
        });
        listMemoryAdapter.setHasStableIds(true);

        listMemoryView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listMemoryView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        listMemoryView.setAdapter(listMemoryAdapter);
        listMemoryView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    addMemory.setVisibility(View.GONE);
                } else if (dy < 0) {
                    addMemory.setVisibility(View.VISIBLE);
                }
            }
        });

        addMemory.setOnClickListener(this);

        changeDateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                listMemoryAdapter.updateStartDate();
            }
        };
        getActivity().registerReceiver(changeDateReceiver, new IntentFilter(Constant.CHANGE_NOTI));
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
    public void onClick(View v) {
        dialog = new AddMemoryDialog(getActivity(), new AddMemoryDialog.OkClickListener() {
            @Override
            public void okClicked(String date, String anniversary, long days, String image64) {
                Memory memory = new Memory(date, anniversary, days, image64);
                listMemory.add(memory);
                listMemoryAdapter.notifyDataSetChanged();
                db.addMemory(memory);
            }

            @Override
            public void photoClicked() {
                pickImage();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();

                CropImage.activity(imageUri)
                        .start(getContext(), this);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    Bitmap selectedImage = Constant.decodeUri(getActivity(), resultUri);
                    dialog.setImage(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (changeDateReceiver != null) {
            getActivity().unregisterReceiver(changeDateReceiver);
        }
    }
}
