package com.test.cameraandvideo.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.test.cameraandvideo.model.ModelImages;
import com.test.cameraandvideo.model.PathItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageLoadUtils {

    public static ArrayList<ModelImages> modelImages = new ArrayList<>();
    boolean boolean_folder;
    List<PathItem> pathAll = new ArrayList<>();


    public List<ModelImages> listImagePath(Context context) {
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


        //create folder
        modelImages.clear();
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
                modelImages.add(obj_model);
            }
            pathAll.add(new PathItem(absolutePathOfImage, false));
        }
        modelImages.add(new ModelImages("All", pathAll, false, false ));

        //sorting
        Collections.sort(modelImages, Comparator.comparing(ModelImages::getFolder));

        return modelImages;
    }

    public List<ModelImages> getListImage(){
       return modelImages;
    }

    public Bitmap getLastImage(Context context){
        Bitmap bm = null;
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {
                bm = BitmapFactory.decodeFile(imageLocation);
            }
        }
        return bm;
    }
}
