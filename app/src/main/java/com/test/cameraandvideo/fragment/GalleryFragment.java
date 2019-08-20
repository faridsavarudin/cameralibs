package com.test.cameraandvideo.fragment;


import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.cameraandvideo.ui.MainActivity;
import com.test.cameraandvideo.R;
import com.test.cameraandvideo.adapter.GalleryAdapter;
import com.test.cameraandvideo.adapter.GalleryItemAdapter;
import com.test.cameraandvideo.model.ModelImages;
import com.test.cameraandvideo.model.PathItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements GalleryAdapter.OnItemClick, GalleryItemAdapter.OnItemClickItem {
    public static ArrayList<ModelImages> modelImages = new ArrayList<>();
    boolean boolean_folder;
    private GalleryAdapter adapter;
    ImageView imgClose;
    private GalleryItemAdapter adapterItem;
    private RecyclerView recyclerView, recyclerItem;
    List<PathItem> pathAll = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

      //  enterFullscreen();
        recyclerView = v.findViewById(R.id.recycler);
        imgClose = v.findViewById(R.id.img_close);
        recyclerItem = v.findViewById(R.id.recycler_item);

        listImagePath();

        imgClose.setOnClickListener(onclick->{
            ((MainActivity)getActivity()).loadFragment(new PhotoFragment());
        });

        return v;
    }

    public List<ModelImages> listImagePath() {
        modelImages.clear();

        recyclerItem.setLayoutManager(new GridLayoutManager(getContext(), 3));

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getActivity().getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


        //create folder
        while (cursor.moveToNext()) {
            ModelImages obj_model = new ModelImages();
            absolutePathOfImage = cursor.getString(column_index_data);
            for (int i = 0; i < modelImages.size(); i++) {
                if (modelImages.get(i).getFolder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }

            if (boolean_folder) {
                List<PathItem> pathItem = new ArrayList<>();

                pathItem.addAll(modelImages.get(int_position).getPathItem());

                pathItem.add(new PathItem(absolutePathOfImage, false));
                modelImages.get(int_position).setPathItem(pathItem);

            } else {
                List<PathItem> al_path = new ArrayList<>();
                al_path.add(new PathItem(absolutePathOfImage, false));
                obj_model.setFolder(cursor.getString(column_index_folder_name));
                obj_model.setPathItem(al_path);
                obj_model.setSelected(false);
                obj_model.setSelectedItem(false);
                modelImages.add(obj_model);
            }
            pathAll.add(new PathItem(absolutePathOfImage, false));
        }
        modelImages.add(new ModelImages("All", pathAll, false, false ));

        //sorting
        Collections.sort(modelImages, Comparator.comparing(ModelImages::getFolder));

        adapter = new GalleryAdapter(getContext(),modelImages, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setActivated(true, 0);

        adapterItem = new GalleryItemAdapter(getContext(),modelImages, 0, this);
        recyclerItem.setAdapter(adapterItem);
        return modelImages;
    }

    @Override
    public void itemClick(int position) {
        adapterItem = new GalleryItemAdapter(getContext(),modelImages, position, this);
     //   adapterItem.setClearSelected();
        recyclerItem.setAdapter(adapterItem);
        adapter.setActivated(true, position);
    }


    @Override
    public void itemClickItem(int position) {
        adapterItem.setActivated(position);
    }

    private void enterFullscreen() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setBackgroundColor(Color.BLACK);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
