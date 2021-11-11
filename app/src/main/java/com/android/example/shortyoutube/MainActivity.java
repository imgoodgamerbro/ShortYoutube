package com.android.example.shortyoutube;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelCollectionAdapter;
import com.android.example.shortyoutube.classes.Channel;
import com.android.example.shortyoutube.databinding.ActivityMainBinding;
import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Channel>>,
        ChannelCollectionAdapter.ChannelItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String YOUTUBE_REQUEST_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&type=channel&key=";
    private static final int CHANNEL_LOADER_ID = 1;

    private ActivityMainBinding mBinding;
    private ChannelCollectionAdapter mAdapter;

    private ArrayList<Channel> channels = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        channels = new ArrayList<Channel>();

        mAdapter = new ChannelCollectionAdapter(this, channels, this);
        mBinding.gvMainActivity.setAdapter(mAdapter);


        initialiseBuilder();

        mBinding.swipeRefresh.setOnRefreshListener(this);
    }

    private void initialiseBuilder(){
        if (!AppUtils.isNetworkAvailable(this)) {
             mBinding.gvMainActivity.setVisibility(View.GONE);
             mBinding.rlNoNetwork.setVisibility(View.VISIBLE);
             mBinding.bNoNetwork.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     initialiseBuilder();
                 }
             });
        } else {
            mBinding.rlNoNetwork.setVisibility(View.GONE);
            LoaderManager loaderManager = getLoaderManager();
            if(loaderManager == null){
                loaderManager.initLoader(CHANNEL_LOADER_ID, null, this);
            }
            else {
                loaderManager.restartLoader(CHANNEL_LOADER_ID, null, this);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Channel>> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<Channel>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mBinding.gvMainActivity.setVisibility(View.GONE);
                mBinding.shimmer.setVisibility(View.VISIBLE);
                mBinding.shimmer.startShimmer();

                Log.d("LOADER", "FETCHING NEW DATA");
                forceLoad();
            }

            @Override
            public List<Channel> loadInBackground() {
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
    public void onLoadFinished(Loader<List<Channel>> loader, List<Channel> data) {
        mBinding.swipeRefresh.setRefreshing(false);
        mBinding.shimmer.stopShimmer();
        mBinding.shimmer.setVisibility(View.GONE);
        mBinding.gvMainActivity.setVisibility(View.VISIBLE);
        Log.d("LOADING", "LOAD FINISHED");

        mAdapter.clear();

        if (data != null) {
            mAdapter.addAll(data);
        } else {
            Toast.makeText(MainActivity.this, "Poor Request", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Channel>> loader) {
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
        if (!AppUtils.isNetworkAvailable(this)) {
            initialiseBuilder();
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


    @Override
    public void onRefresh() {
        Log.d("MAIN", "REFRESHING...");
        if (AppUtils.isNetworkAvailable(this)) {
            getLoaderManager().restartLoader(CHANNEL_LOADER_ID, null, this);
        } else {
            initialiseBuilder();
        }
    }
}