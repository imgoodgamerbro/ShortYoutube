package com.android.example.shortyoutube.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;
import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelDetailsCollectionAdapter;
import com.android.example.shortyoutube.classes.ChannelDetailsCollection;
import com.android.example.shortyoutube.databinding.FragmentDetailBinding;

import java.io.IOException;
import java.net.URL;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<ChannelDetailsCollection> {

    private Bundle mBundle;
    private FragmentDetailBinding mBinding;
    private ChannelDetailsCollectionAdapter mAdapter;

    private String mChannelId, mChannelImage, mChannelTitle, mChannelDes;

    private static final String CHANNEL_DETAILS_REQUEST_URL = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails%2Cstatistics&id=";
    private static final String KEY = "&key=";
    private static final int CHANNEL_DETAILS_LOADER_ID = 1;

    public DetailFragment(Bundle bundle){
        mBundle = bundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false);
        View rootView = mBinding.getRoot();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvDetailActivity.setLayoutManager(layoutManager);

        mBinding.rvDetailActivity.setHasFixedSize(true);
        mAdapter = new ChannelDetailsCollectionAdapter();
        mBinding.rvDetailActivity.setAdapter(mAdapter);

        if(mBundle != null){
            mChannelId  = mBundle.getString("id");
            mChannelImage  = mBundle.getString("thumbnail");
            mChannelTitle  = mBundle.getString("title");
            mChannelDes  = mBundle.getString("des");
        }

        Loader();
        return rootView;
    }

    public void Loader(){
        getLoaderManager().initLoader(CHANNEL_DETAILS_LOADER_ID,null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<ChannelDetailsCollection> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ChannelDetailsCollection>(requireContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mBinding.loadingIndicator.setVisibility(View.VISIBLE);

                Log.d("LOADER-Detail Activity", "FETCHING NEW DATA");
                forceLoad();
            }

            @Nullable
            @Override
            public ChannelDetailsCollection loadInBackground() {
                String searchResults = null;
                try {
                    URL channelsUrl = new URL(CHANNEL_DETAILS_REQUEST_URL + mChannelId + KEY);
                    searchResults = AppUtils.getResponseFromHttpUrl(channelsUrl);
                    Log.d("DetailFragment Url", searchResults);
                    if(mChannelId != null){
                        Log.d("DetailFragmentId", mChannelId);
                    } else {
                        Log.d("DetailFragmentId", "null Id");
                    }
                    return JsonParsing.parseJsonChannelData(searchResults, mChannelImage, mChannelTitle, mChannelDes);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ChannelDetailsCollection> loader, ChannelDetailsCollection data) {
        mBinding.loadingIndicator.setVisibility(View.INVISIBLE);
        Log.d("LOADING-DetailActivity", "LOAD FINISHED");

        if(data != null){
            mAdapter.appendData(data);
        } else {
            Toast.makeText(getContext(), "Poor Request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ChannelDetailsCollection> loader) {

    }
}