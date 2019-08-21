package com.test.cameraandvideo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.test.cameraandvideo.R;

import java.io.File;

public class PostReviewActivity extends AppCompatActivity {

    File file;
    private Spinner spCollections;
    private String[] collections = {
            "Select Collection",
            "Collection 2",
            "Collection 3"
    };
    ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);
        spCollections  = findViewById(R.id.sp_collection);
        imgPreview = findViewById(R.id.img_preview);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, collections);
        spCollections.setAdapter(adapter);
        file = (File)getIntent().getExtras().get("file");
        Glide.with(this).load(file).into(imgPreview);

        getSupportActionBar().hide();
    }
}
