package com.android.example.shortyoutube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;
import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelCollectionAdapter;
import com.android.example.shortyoutube.classes.ChannelCollection;
import com.android.example.shortyoutube.databinding.ActivitySearchBinding;

import java.io.IOException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity implements
        ChannelCollectionAdapter.ChannelItemClickListener,
        LoaderManager.LoaderCallbacks<ChannelCollection> {

    private static final String YOUTUBE_REQUEST_URL_1 = "https://www.googleapis.com/youtube/v3/search?q=";
    private static final String YOUTUBE_REQUEST_URL_2 = "&order=viewCount&part=snippet&maxResults=10&type=channel&key=AIzaSyDQsCcN3gT2ROts4di-FQMI6-XHUbKI4Q0";
    private static final int CHANNEL_LOADER_ID = 2;

    private ActivitySearchBinding mBinding;
    private ChannelCollectionAdapter mAdapter;

    private Bundle mBundle;
    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getOrientationGridSpans());
        mBinding.rvSearchActivity.setLayoutManager(layoutManager);

        mBinding.rvSearchActivity.setHasFixedSize(true);
        mAdapter = new ChannelCollectionAdapter(this);
        mBinding.rvSearchActivity.setAdapter(mAdapter);


        ImageView leftIcon = findViewById(R.id.back_image);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mEditText = findViewById(R.id.editText);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchButton(v);
                    return true;
                }
                return false;
            }
        });

//        if (AppUtils.isNetworkAvailable(this)) {
//            LoaderManager loaderManager = getLoaderManager();
//            loaderManager.initLoader(CHANNEL_LOADER_ID, null, this);
//        } else {
//            finish();
//        }
    }

    private int getOrientationGridSpans() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 2;
        else
            return 1;
    }

    public void searchButton(View v) {
        if (AppUtils.isNetworkAvailable(this)) {
            Loader<String> loader = getLoaderManager().getLoader(CHANNEL_LOADER_ID);
            String query = mEditText.getText().toString().trim();
            mBundle = new Bundle();
            mBundle.putString("query", query);

            mBinding.rvSearchActivity.setVisibility(View.GONE);
            mBinding.emptyTextView.setVisibility(View.VISIBLE);
            mBinding.emptyTextView.setText("");

            if(loader == null){
                getLoaderManager().initLoader(CHANNEL_LOADER_ID, mBundle, this);
            }
            else{
                getLoaderManager().restartLoader(CHANNEL_LOADER_ID, mBundle, this);
            }
        } else {
            finish();
        }
    }


    private String ACTUAL_URL = "";

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ChannelCollection> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            if (!args.getString("query").isEmpty()) {
                Log.d("SearchActivity", "Query" + args.getString("query").trim());
                ACTUAL_URL = YOUTUBE_REQUEST_URL_1 + args.getString("query").trim() + YOUTUBE_REQUEST_URL_2;
            } else {
                ACTUAL_URL = YOUTUBE_REQUEST_URL_1 + YOUTUBE_REQUEST_URL_2;
            }
        } else {
            ACTUAL_URL = YOUTUBE_REQUEST_URL_1 + YOUTUBE_REQUEST_URL_2;
        }
        return new AsyncTaskLoader<ChannelCollection>(this) {

            @Override
            public ChannelCollection loadInBackground() {
                String searchResults = null;
                try {
                    URL channelsUrl = new URL(ACTUAL_URL);
                    searchResults = AppUtils.getResponseFromHttpUrl(channelsUrl);
                    //Log.d("Searched Url", searchResults);
                    return JsonParsing.parseJsonData(searchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mBinding.loader.setVisibility(View.VISIBLE);
                Log.d("LOADER", "FETCHING NEW DATA");
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ChannelCollection> loader, ChannelCollection data) {
        mBinding.loader.setVisibility(View.GONE);
        mBinding.emptyTextView.setVisibility(View.GONE);
        mBinding.rvSearchActivity.setVisibility(View.VISIBLE);
        Log.d("LOADING", "LOAD FINISHED");


        if (data != null){ //&& !data.isEmpty()) {
            mAdapter.appendCollection(data);
            Log.d("URL", ACTUAL_URL);
            Log.d("EditText", mEditText.getText().toString());
        } else {
            Toast.makeText(SearchActivity.this, "Poor Request", Toast.LENGTH_SHORT).show();
            Log.d("URL", ACTUAL_URL);
            Log.d("EditText", mEditText.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<ChannelCollection> loader){
        //mAdapter.clearData();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getLoaderManager().restartLoader(CHANNEL_LOADER_ID, mBundle, this);
//    }

    @Override
    public void onChannelClick(int clickedItemIndex, String id, String imageUrl, String title, String des) {
        Intent intent = new Intent(SearchActivity.this, CustomActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("thumbnail", imageUrl);
        bundle.putString("title", title);
        bundle.putString("des", des);

        intent.putExtras(bundle);
        startActivity(intent);
    }
}