package com.test.cameraandvideo.ui;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.test.cameraandvideo.R;
import com.test.cameraandvideo.adapter.ContainerAdapter;
import com.test.cameraandvideo.fragment.GalleryFragment;
import com.test.cameraandvideo.fragment.PhotoFragment;
import com.test.cameraandvideo.model.Container;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<Container> arrayList = new ArrayList();
    ContainerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        loadFragment(new PhotoFragment());

        addList();
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment, PhotoFragment.class.getName())
                    .commit();

            if (fragment instanceof GalleryFragment){
                recyclerView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                adapter.setColorGray(true);
            }else {
                if (adapter!=null)
                adapter.setColorGray(false);
                recyclerView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }



    private void addList() {
        arrayList.add(new Container("Stopmortion"));
        arrayList.add(new Container("Video"));
        arrayList.add(new Container("Photo"));
        arrayList.add(new Container("AR Image"));
        arrayList.add(new Container("AR Object"));

        adapter = new ContainerAdapter(arrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setColorGray(false);
    }
}
