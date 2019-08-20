package com.test.cameraandvideo.ui;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.test.cameraandvideo.R;
import com.test.cameraandvideo.collageviews.MultiTouchListener;
import com.test.cameraandvideo.options.Commons;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    ImageView collageView;
    private List<File> mediaFiles = new ArrayList<>();
    MultiTouchListener multiTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        collageView = findViewById(R.id.collageView1);
        multiTouchListener = new MultiTouchListener();

        collageView.setOnTouchListener(multiTouchListener);


        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;
        Log.e("maxx", maxX+"");
        Log.e("maxY", maxY+"");

        getFiles();
        enterFullscreen();
    }


    private void getFiles() {
        File file = new File(Commons.MEDIA_DIR);
        if (file.isDirectory()) {
            mediaFiles.clear();
            File[] files = file.listFiles();
            mediaFiles.addAll(Arrays.asList(files));
            file = mediaFiles.get(mediaFiles.size()-1);
            Glide.with(this).load(file).into(collageView);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if (event.equalsIgnoreCase("gesture")) {
            int[] location = new int[2];
            collageView.getLocationInWindow(location);
            int x = location[0];
            int y = location[1];
            Log.e("position x", collageView.getX() + "");
            Log.e("position y", collageView.getY() + "");
            Log.e("scale X", collageView.getScaleX() + "");
            Log.e("scale y", collageView.getScaleY() + "");
        }
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
}
