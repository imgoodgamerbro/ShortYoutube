package com.android.example.shortyoutube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.shortyoutube.adapters.CustomAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        String result = sharedPreferences.getString(getString(R.string.shared_preference_channel_video_list), getString(R.string.default_value_int));

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

        Bundle videoBundle = new Bundle();
        videoBundle.putString("id", id);
        videoBundle.putString("result", result);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        CustomAdapter adapter = new CustomAdapter(this, getSupportFragmentManager(), detailBundle, videoBundle);
        viewPager.setAdapter(adapter);

        TabLayout tab = (TabLayout) findViewById(R.id.tabs);
        tab.setupWithViewPager(viewPager);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ImageView imageView = findViewById(R.id.back_image_custom);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView textView = findViewById(R.id.tv_action_bar);
        textView.setText(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}