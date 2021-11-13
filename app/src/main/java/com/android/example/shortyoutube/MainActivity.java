package com.android.example.shortyoutube;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

    //Api - https://www.googleapis.com/youtube/v3/search?q=bb&part=snippet&maxResults=5&type=channel&key=Enter Api key

    private static final int CHANNEL_LOADER_ID = 1;

    private ActivityMainBinding mBinding;
    private ChannelCollectionAdapter mAdapter;

    SharedPreferences mSharedPreferences;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        boolean aBoolean = mSharedPreferences.getBoolean(getString(R.string.shared_preference_night_mode), false);
        if(!aBoolean){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        aBoolean = mSharedPreferences.getBoolean(getString(R.string.shared_preference_night_mode), false);
        if(aBoolean){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        ArrayList<Channel> channels = new ArrayList<>();
        mAdapter = new ChannelCollectionAdapter(this, channels, this);
        mBinding.gvMainActivity.setAdapter(mAdapter);

        initialiseLoader();

        mBinding.swipeRefresh.setOnRefreshListener(this);
    }

    private void initialiseLoader(){
        if (!AppUtils.isNetworkAvailable(this)) {
             mBinding.gvMainActivity.setVisibility(View.GONE);
             mBinding.rlNoNetwork.setVisibility(View.VISIBLE);
             mBinding.bNoNetwork.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     initialiseLoader();
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

    private String YOUTUBE_REQUEST_URL = "";
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Channel>> onCreateLoader(int id, Bundle args) {
        mSharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        result = mSharedPreferences.getString(getString(R.string.shared_preference_channel_list), getString(R.string.default_value_int));
        YOUTUBE_REQUEST_URL = getString(R.string.main_activity_url_1)+ result + getString(R.string.main_activity_url_2);
        Log.d("MainActivityResult", "" + result);

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
                String searchResults;
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

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            Toast.makeText(MainActivity.this, "Try Again later", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Channel>> loader) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppUtils.isNetworkAvailable(this)) {
            getLoaderManager().restartLoader(CHANNEL_LOADER_ID, null, this);
        } else {
            initialiseLoader();
        }
    }

    @Override
    public void onRefresh() {
        Log.d("MAIN", "REFRESHING...");
        if (AppUtils.isNetworkAvailable(this)) {
            getLoaderManager().restartLoader(CHANNEL_LOADER_ID, null, this);
        } else {
            initialiseLoader();
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
        } else{
            if(item.getItemId() == R.id.settings){
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}