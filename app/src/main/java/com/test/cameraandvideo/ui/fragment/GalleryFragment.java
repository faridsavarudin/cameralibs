package com.test.cameraandvideo.ui.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.cameraandvideo.ui.MainActivity;
import com.test.cameraandvideo.R;
import com.test.cameraandvideo.adapter.GalleryAdapter;
import com.test.cameraandvideo.adapter.GalleryItemAdapter;
import com.test.cameraandvideo.model.ModelImages;
import com.test.cameraandvideo.model.PathItem;
import com.test.cameraandvideo.ui.PreviewActivity;
import com.test.cameraandvideo.utils.ImageLoadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleryFragment extends Fragment implements GalleryAdapter.OnItemClick, GalleryItemAdapter.OnItemClickItem {
    public static List<ModelImages> modelImages = new ArrayList<>();
    private GalleryAdapter adapter;
    ImageView imgClose;
    private GalleryItemAdapter adapterItem;
    private RecyclerView recyclerView, recyclerItem;
    private String imageSelected;
    private TextView tvAdd;
    ImageLoadUtils imageLoadUtils;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = v.findViewById(R.id.recycler);
        imgClose = v.findViewById(R.id.img_close);
        tvAdd = v.findViewById(R.id.tv_add);
        recyclerItem = v.findViewById(R.id.recycler_item);

        imageLoadUtils = new ImageLoadUtils();
        modelImages.clear();
        recyclerItem.setLayoutManager(new GridLayoutManager(getContext(), 3));

        modelImages = imageLoadUtils.listImagePath(getContext());

        adapter = new GalleryAdapter(getContext(), modelImages, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setActivated(true, 0);

        adapterItem = new GalleryItemAdapter(getContext(), modelImages, 0, this);
        recyclerItem.setAdapter(adapterItem);

        imgClose.setOnClickListener(onclick -> {
            ((MainActivity) getActivity()).loadFragment(new PhotoFragment());
        });

        tvAdd.setOnClickListener(onclick -> {
            startActivity(PreviewActivity.generateIntent(getContext(), imageSelected));
        });

        return v;
    }



    @Override
    public void itemClick(int position) {
        adapter.setActivated(true, position);
        adapterItem = new GalleryItemAdapter(getContext(), modelImages, position, this);
        recyclerItem.setAdapter(adapterItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        enterFullscreen();
    }

    @Override
    public void itemClickItem(int getPosition, int position) {
        imageSelected = modelImages.get(getPosition).getPathItem().get(position).getPath();
        if (imageSelected != null) {
            tvAdd.setVisibility(View.VISIBLE);
        } else
            tvAdd.setVisibility(View.GONE);
        adapterItem.setActivated(getPosition, position);
    }

    @Override
    public void unselected() {
        tvAdd.setVisibility(View.GONE);
    }

    private void enterFullscreen() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setBackgroundColor(Color.BLACK);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
