package com.android.example.shortyoutube;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelCollectionAdapter;
import com.android.example.shortyoutube.classes.ChannelCollection;
import com.android.example.shortyoutube.databinding.ActivityMainBinding;
import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;
import com.android.example.shortyoutube.fragments.DetailFragment;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ChannelCollection>,
        ChannelCollectionAdapter.ChannelItemClickListener {

    private static final String YOUTUBE_REQUEST_URL = "https://www.googleapis.com/youtube/v3/search?q=bbkivines&part=snippet&maxResults=5&type=channel&key=AIzaSyAGM5XCNHnfKaXdNUF__CKopLcKJrWyFdY";
    private static final int CHANNEL_LOADER_ID = 1;

    private ActivityMainBinding mBinding;
    private ChannelCollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getOrientationGridSpans());
        mBinding.rvMainActivity.setLayoutManager(layoutManager);

        mBinding.rvMainActivity.setHasFixedSize(true);
        mAdapter = new ChannelCollectionAdapter(this);
        mBinding.rvMainActivity.setAdapter(mAdapter);

        if (AppUtils.isNetworkAvailable(this)) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(CHANNEL_LOADER_ID, null, this);
        } else {
            finish();
        }
    }

    private int getOrientationGridSpans() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 2;
        else
            return 1;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ChannelCollection> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<ChannelCollection>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mBinding.loadingIndication.setVisibility(View.VISIBLE);

                Log.d("LOADER", "FETCHING NEW DATA");
                forceLoad();
            }

            @Override
            public ChannelCollection loadInBackground() {
                String searchResults = null;
                try {
                    URL channelsUrl = new URL(YOUTUBE_REQUEST_URL);
                    searchResults = AppUtils.getResponseFromHttpUrl(channelsUrl);
                    return JsonParsing.parseJsonData(searchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ChannelCollection> loader, ChannelCollection data) {
        mBinding.loadingIndication.setVisibility(View.INVISIBLE);
        Log.d("LOADING", "LOAD FINISHED");


        if (data != null && !data.isEmpty()) {
            mAdapter.appendCollection(data);
        } else {
            Toast.makeText(MainActivity.this, "Poor Request", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<ChannelCollection> loader) {
    }

    @Override
    public void onChannelClick(int clickedItemIndex, String id, String imageUrl, String title, String des) {
        Intent intent = new Intent(MainActivity.this, CustomActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("thumbnail", imageUrl);
        bundle.putString("title", title);
        bundle.putString("des", des);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.isNetworkAvailable(this)) {
            getLoaderManager().restartLoader(CHANNEL_LOADER_ID, null, this);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search_bar) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}