package com.test.cameraandvideo.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.test.cameraandvideo.ui.MainActivity;
import com.test.cameraandvideo.ui.PreviewActivity;
import com.test.cameraandvideo.adapter.SizeAdapter;
import com.test.cameraandvideo.model.Container;
import com.test.cameraandvideo.R;
import com.test.cameraandvideo.adapter.ContainerAdapter;
import com.test.cameraandvideo.options.AspectRatioItem;
import com.test.cameraandvideo.options.Commons;

import java.util.ArrayList;
import java.util.List;

import top.defaults.camera.AspectRatio;
import top.defaults.camera.CameraView;
import top.defaults.camera.Photographer;
import top.defaults.camera.PhotographerFactory;
import top.defaults.camera.PhotographerHelper;


public class PhotoFragment extends Fragment implements SizeAdapter.OnItemClickListener {

    private List<Container> arrayList = new ArrayList();
    RecyclerView  recyclerViewSize;
    ImageView imgCrop, imgCropList, imgPicture, imgBack, imgFlash, imgGallery;
    ContainerAdapter adapter;
    Photographer photographer;
    PhotographerHelper photographerHelper;
    CameraView cameraView;
    int top, bottom;
    SizeAdapter adapterSize;
    TextView txtDone;
    List<AspectRatioItem> supportedAspectRatios;
    List<AspectRatioItem> supportedAspectRatios2;
    AspectRatioItem itemsSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
       // recyclerView = view.findViewById(R.id.recycler);
        recyclerViewSize = view.findViewById(R.id.list_ratio);
        imgCrop = view.findViewById(R.id.img_crop);
        imgBack = view.findViewById(R.id.img_back);
        imgFlash = view.findViewById(R.id.img_flash);
        imgGallery = view.findViewById(R.id.img_gallery);
        imgCropList = view.findViewById(R.id.img_crop_list);
        txtDone = view.findViewById(R.id.txt_done);
        imgPicture = view.findViewById(R.id.img_picture);
        cameraView = view.findViewById(R.id.preview);

        permission();
        return view;
    }

    private void permission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onResume() {
        super.onResume();
        enterFullscreen();
        photographer.startPreview();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        photographer = PhotographerFactory.createPhotographerWithCamera2(getActivity(), cameraView);
        photographerHelper = new PhotographerHelper(photographer);
        photographerHelper.setFileDir(Commons.MEDIA_DIR);

        imgCrop.setOnClickListener(v -> {
            supportedAspectRatios = Commons.wrapItems(photographer.getSupportedAspectRatios(), AspectRatioItem::new);
            supportedAspectRatios2 = new ArrayList<>();

            for (AspectRatioItem ratioItem : supportedAspectRatios) {
                if (ratioItem.get().toString().equalsIgnoreCase("16:9")) {
                    ratioItem.setImg(R.drawable.nine_sixteen_unselected);
                    supportedAspectRatios2.add(ratioItem);
                }
                if (ratioItem.get().toString().equalsIgnoreCase("3:2")) {
                    ratioItem.setImg(R.drawable.two_three_unselected);
                    supportedAspectRatios2.add(ratioItem);
                }
                if (ratioItem.get().toString().equalsIgnoreCase("4:3")) {
                    ratioItem.setImg(R.drawable.three_four_unselected);
                    supportedAspectRatios2.add(ratioItem);
                }
                if (ratioItem.get().toString().equalsIgnoreCase("1:1")) {
                    ratioItem.setImg(R.drawable.one_one_unselected);
                    supportedAspectRatios2.add(ratioItem);
                }
            }

            recyclerViewSize.setVisibility(View.VISIBLE);
            imgCrop.setVisibility(View.GONE);
            imgCropList.setVisibility(View.VISIBLE);
            txtDone.setVisibility(View.VISIBLE);
            imgBack.setVisibility(View.GONE);
            imgFlash.setVisibility(View.GONE);

            adapterSize = new SizeAdapter(supportedAspectRatios2, this);
            recyclerViewSize.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewSize.setAdapter(adapterSize);
        });

        imgPicture.setOnClickListener(v -> {
            photographer.takePicture();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getContext(), PreviewActivity.class));
                }
            }, 1000);

        });

        txtDone.setOnClickListener(v -> {
            imgCrop.setVisibility(View.VISIBLE);
            imgCropList.setVisibility(View.GONE);
            txtDone.setVisibility(View.GONE);
            recyclerViewSize.setVisibility(View.GONE);
            imgBack.setVisibility(View.VISIBLE);
            imgFlash.setVisibility(View.VISIBLE);

            if (itemsSelected==null) {
                photographer.setAspectRatio(photographer.getAspectRatio());
                checkRatio(photographer.getAspectRatio());
                return;
            }
            photographer.setAspectRatio(itemsSelected.get());
            checkRatio(itemsSelected.get());
        });

        imgGallery.setOnClickListener(v->{
            ((MainActivity)getActivity()).loadFragment(new GalleryFragment());
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void checkRatio(AspectRatio aspectRatio) {
        if (aspectRatio.toString().equalsIgnoreCase("4:3")) {
            top = getPixelValue(getContext(), 47);
            setMargins(cameraView, 0, top, 0, 0);
            cameraView.setPositionTop();
        }
        if (aspectRatio.toString().equalsIgnoreCase("1:1")) {
            top = getPixelValue(getContext(), 47);
            bottom = getPixelValue(getContext(), 120);
            setMargins(cameraView, 0, top, 0, bottom);
            cameraView.setPositionCenter();
        }

        if (aspectRatio.toString().equalsIgnoreCase("3:2")) {
            top = getPixelValue(getContext(), 63);
            setMargins(cameraView, 0, top, 0, 0);
            cameraView.setPositionTop();
        }
        if (aspectRatio.toString().equalsIgnoreCase("16:9")) {
            setMargins(cameraView, 0, 0, 0, 0);
            cameraView.setPositionTop();
        }

    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }


    private void enterFullscreen() {
        View decorView = getActivity().getWindow().getDecorView();
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
    public void onItemClick(AspectRatioItem item, int position) {
        itemsSelected = item;
        photographer.setAspectRatio(itemsSelected.get());
        checkRatio(itemsSelected.get());
    }
}
