package com.test.cameraandvideo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.test.cameraandvideo.R;

import java.io.File;

public class ImageLoader {
    private Context context;
    ImageView image ;

    public ImageLoader(Context context) {
        this.context = context;
        image = new ImageView(context);
    }

    public RelativeLayout loadImages(String file){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate( R.layout.item_image, null );
        ImageView imageView = view.findViewById(R.id.img_cover);
        RelativeLayout containerMedia = view.findViewById(R.id.container_media);

        Glide.with(context).load(file).into(imageView);

        return containerMedia;
    }
}
