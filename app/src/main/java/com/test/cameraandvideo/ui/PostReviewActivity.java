package com.test.cameraandvideo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.test.cameraandvideo.R;

public class PostReviewActivity extends AppCompatActivity {

    private Spinner spCollections;
    private String[] collections = {
            "Collection 1",
            "Collection 2",
            "Collection 3"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);
        spCollections  = findViewById(R.id.sp_collection);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, collections);
        spCollections.setAdapter(adapter);


        getSupportActionBar().hide();
    }
}
