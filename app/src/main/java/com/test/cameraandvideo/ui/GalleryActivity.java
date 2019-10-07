package com.test.cameraandvideo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.cameraandvideo.R;
import com.test.cameraandvideo.adapter.GalleryAdapter;
import com.test.cameraandvideo.adapter.GalleryItemAdapter;
import com.test.cameraandvideo.adapter.GalleryItemAdapterMultiple;
import com.test.cameraandvideo.model.ModelImages;
import com.test.cameraandvideo.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.OnItemClick, GalleryItemAdapterMultiple.OnItemClickItem {

    public static List<ModelImages> modelImages = new ArrayList<>();
    private GalleryAdapter adapter;
    ImageView imgClose;
    private GalleryItemAdapterMultiple adapterItem;
    private RecyclerView recyclerView, recyclerItem;
    private String imageSelected;
    private TextView tvAdd;
    ImageLoadUtils imageLoadUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        init();

        recyclerItem.setLayoutManager(new GridLayoutManager(this, 3));
        modelImages = imageLoadUtils.listImagePath(this);

        adapter = new GalleryAdapter(this, modelImages, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setActivated(true, 0);

        adapterItem = new GalleryItemAdapterMultiple(this, modelImages, 0, this);
        recyclerItem.setAdapter(adapterItem);

        tvAdd.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("MESSAGE", imageSelected);
            setResult(2, intent);
            finish();
        });

        imgClose.setOnClickListener(v->{
            finish();
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler);
        imgClose = findViewById(R.id.img_close);
        tvAdd = findViewById(R.id.tv_add);
        recyclerItem = findViewById(R.id.recycler_item);
        imageLoadUtils = new ImageLoadUtils();
        modelImages.clear();
    }


    @Override
    protected void onResume() {
        super.onResume();
        enterFullscreen();
    }

    private void enterFullscreen() {
        View decorView = getWindow().getDecorView();
        decorView.setBackgroundColor(Color.BLACK);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void itemClick(int position) {
        adapter.setActivated(true, position);
        adapterItem = new GalleryItemAdapterMultiple(this, modelImages, position, this);
        recyclerItem.setAdapter(adapterItem);
    }

    @Override
    public void itemClickItem(int getParentPosition, int getPosition) {
        imageSelected = modelImages.get(getParentPosition).getPathItem().get(getPosition).getPath();
        if (imageSelected != null) {
        tvAdd.setVisibility(View.VISIBLE);
        } else
            tvAdd.setVisibility(View.GONE);
        adapterItem.setActivated(getParentPosition, getPosition);
    }

    @Override
    public void unselected() {
        tvAdd.setVisibility(View.GONE);
    }
}
