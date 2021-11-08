package com.android.example.shortyoutube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.android.example.shortyoutube.adapters.CustomAdapter;
import com.google.android.material.tabs.TabLayout;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String imageUrl = bundle.getString("thumbnail");
        String title = bundle.getString("title");
        String des = bundle.getString("des");

        Bundle detailBundle = new Bundle();
        detailBundle.putString("id", id);
        detailBundle.putString("thumbnail", imageUrl);
        detailBundle.putString("title", title);
        detailBundle.putString("des", des);
//        Fragment fragment1 = new DetailFragment();
//        fragment1.setArguments(detailBundle);

        Bundle videoBundle = new Bundle();
        videoBundle.putString("id", id);
//        Fragment fragment2 = new VideoFragment();
//        fragment2.setArguments(videoBundle);

        ViewPager  viewPager = (ViewPager) findViewById(R.id.viewPager);

        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager(), detailBundle, videoBundle);
        viewPager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);
    }
}