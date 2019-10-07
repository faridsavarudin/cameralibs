package com.test.cameraandvideo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.cameraandvideo.R;
import com.test.cameraandvideo.collageviews.MultiTouchListener;
import com.test.cameraandvideo.options.Commons;
import com.test.cameraandvideo.utils.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    private static final String FILE = "FILE";
    RelativeLayout collageView;
    ImageView imageView, imgAdd;
    TextView txtX, txtY, txtScale;
    private List<File> mediaFiles = new ArrayList<>();
    MultiTouchListener multiTouchListener;
    private TextView txtNext;
    private RelativeLayout relCapture;
    ImageLoader imageLoader;

    int size = 1;

    public static Intent generateIntent(@NonNull Context context, String file) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(FILE, file);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        collageView = findViewById(R.id.collageView1);
        txtNext = findViewById(R.id.txt_next);
        txtX = findViewById(R.id.positionX);
        imgAdd = findViewById(R.id.img_add);
        txtY = findViewById(R.id.positionY);
        txtScale = findViewById(R.id.scale);
        relCapture = findViewById(R.id.rel_capture);
        multiTouchListener = new MultiTouchListener();

        // collageView.setOnTouchListener(multiTouchListener);
        imageLoader = new ImageLoader(this);

        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, GalleryActivity.class);
            startActivityForResult(intent, 2);
        });

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);

        if (getIntent().getExtras() != null) {
            String url = getIntent().getExtras().getString(FILE);
            imageLoader.loadImages(url);
            RelativeLayout relativeLayout = imageLoader.loadImages(url);

            relativeLayout.setOnTouchListener(multiTouchListener);

            collageView.addView(relativeLayout);
            //  Glide.with(this).load("file://"+url).into(collageView);
        } else {
            getFiles();
        }

        txtNext.setOnClickListener(v -> {
            takeScreenshot();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        enterFullscreen();
    }

    private void getFiles() {
        File file = new File(Commons.MEDIA_DIR);
        if (file.isDirectory()) {
            mediaFiles.clear();
            File[] files = file.listFiles();
            mediaFiles.addAll(Arrays.asList(files));
            file = mediaFiles.get(mediaFiles.size() - 1);
            //      Glide.with(this).load(file).into(collageView);

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
            txtX.setText("X =" + collageView.getX());
            txtY.setText("Y =" + collageView.getY());
            txtScale.setText("Scale =" + collageView.getScaleX());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            {
                if (data != null) {
                    String message = data.getStringExtra("MESSAGE");
                    imageLoader.loadImages(message);
                    RelativeLayout relativeLayout = imageLoader.loadImages(message);
                    relativeLayout.setOnTouchListener(multiTouchListener);
                    collageView.addView(relativeLayout);
                }
            }
        }

    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            String mPath = Commons.MEDIA_DIR + "/" + now + ".jpg";

            relCapture.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(relCapture.getDrawingCache());
            relCapture.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            startIntent(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void startIntent(File imageFile) {
        Intent intent = new Intent(this, PostReviewActivity.class);
        intent.putExtra("file", imageFile);
        startActivity(intent);
    }

}
