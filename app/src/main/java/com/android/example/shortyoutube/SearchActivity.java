package com.android.example.shortyoutube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;
import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelCollectionAdapter;
import com.android.example.shortyoutube.classes.Channel;
import com.android.example.shortyoutube.databinding.ActivitySearchBinding;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Channel>>,
        ChannelCollectionAdapter.ChannelItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int CHANNEL_LOADER_ID = 2;

    private ActivitySearchBinding mBinding;
    private ChannelCollectionAdapter mAdapter;

    private Bundle mBundle;
    private EditText mEditText;

    String change = null, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        result = sharedPreferences.getString(getString(R.string.shared_preference_channel_list), getString(R.string.default_value_int));

        ArrayList<Channel> channels = new ArrayList<>();

        mAdapter = new ChannelCollectionAdapter(this, channels, this);
        mBinding.gvSearchActivity.setAdapter(mAdapter);
        mBinding.searchNoResult.setVisibility(View.GONE);


        ImageView leftIcon = findViewById(R.id.back_image);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEditText = findViewById(R.id.editText);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchButton(v);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    View view = getCurrentFocus();
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mBinding.swipeRefreshSearch.setOnRefreshListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("key_query", mEditText.getText().toString().trim());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        change = savedInstanceState.getString("key_query");
        Log.d("Saved", "" + change);
        searchButton(mEditText);
    }

    public void searchButton(View v) {
        if (AppUtils.isNetworkAvailable(this)) {
            String query;
            Loader<String> loader = getLoaderManager().getLoader(CHANNEL_LOADER_ID);
            if(mEditText != null){
                query = mEditText.getText().toString().trim();
            }else {
                query = change;
            }
            mBundle = new Bundle();
            mBundle.putString("query", query);

            mBinding.searchRlNoNetwork.setVisibility(View.GONE);
            if(loader == null){
                getLoaderManager().initLoader(CHANNEL_LOADER_ID, mBundle, this);
            }
            else{
                getLoaderManager().restartLoader(CHANNEL_LOADER_ID, mBundle, this);
            }
        } else {
            initialiseBuilder();
        }
    }

    private void initialiseBuilder(){
        if (!AppUtils.isNetworkAvailable(this)) {
            mBinding.gvSearchActivity.setVisibility(View.GONE);
            mBinding.searchRlNoNetwork.setVisibility(View.VISIBLE);
            mBinding.searchBNoNetwork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initialiseBuilder();
                }
            });
        } else {
            mBinding.searchRlNoNetwork.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.search_activity_toast_message), Toast.LENGTH_SHORT).show();
        }
    }

    private String ACTUAL_URL = "";
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Channel>> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            if (!args.getString("query").isEmpty()) {
                Log.d("SearchActivity", "Query" + args.getString("query").trim());
                ACTUAL_URL = getString(R.string.search_activity_url_1) + args.getString("query").trim() + getString(R.string.search_activity_url_2) + result + getString(R.string.search_activity_url_3);
            } else {
                ACTUAL_URL = getString(R.string.search_activity_url_1) + getString(R.string.search_activity_url_2) + result + getString(R.string.search_activity_url_3);
            }
        } else {
            ACTUAL_URL = getString(R.string.search_activity_url_1) + getString(R.string.search_activity_url_2) + result + getString(R.string.search_activity_url_3);
        }
        return new AsyncTaskLoader<List<Channel>>(this) {

            @Override
            public List<Channel> loadInBackground() {
                String searchResults;
                try {
                    URL channelsUrl = new URL(ACTUAL_URL);
                    searchResults = AppUtils.getResponseFromHttpUrl(channelsUrl);
                    return JsonParsing.parseJsonData(searchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mBinding.gvSearchActivity.setVisibility(View.GONE);
                mBinding.shimmerSearch.setVisibility(View.VISIBLE);
                mBinding.shimmerSearch.startShimmer();
                mBinding.searchNoResult.setVisibility(View.GONE);
                Log.d("LOADER", "FETCHING NEW DATA");
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Channel>> loader, List<Channel> data) {
        mBinding.swipeRefreshSearch.setRefreshing(false);
        mBinding.shimmerSearch.setVisibility(View.GONE);
        mBinding.shimmerSearch.stopShimmer();
        mBinding.gvSearchActivity.setVisibility(View.VISIBLE);
        Log.d("LOADING", "LOAD FINISHED");

        mAdapter.clear();

        if (data != null && !data.isEmpty()){
            mAdapter.addAll(data);
            Log.d("URL", ACTUAL_URL);
            Log.d("EditText", mEditText.getText().toString());
        } else {
            mBinding.searchNoResult.setVisibility(View.VISIBLE);
            Log.d("URL", ACTUAL_URL);
            Log.d("EditText", mEditText.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Channel>> loader){
    }

    @Override
    public void onRefresh() {
        Log.d("SEARCH", "REFRESHING...");
        getLoaderManager().restartLoader(CHANNEL_LOADER_ID, mBundle, this);
    }


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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}