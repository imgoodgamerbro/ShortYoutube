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

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;
import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelDetailsCollectionAdapter;
import com.android.example.shortyoutube.classes.ChannelDetailsCollection;
import com.android.example.shortyoutube.databinding.FragmentDetailBinding;

import java.io.IOException;
import java.net.URL;

public class DetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ChannelDetailsCollection> {

    private Bundle mBundle;
    private FragmentDetailBinding mBinding;
    private ChannelDetailsCollectionAdapter mAdapter;

    private String mChannelId, mChannelImage, mChannelTitle, mChannelDes;

    private static final int CHANNEL_DETAILS_LOADER_ID = 1;

    public DetailFragment(){
        //Do nothing
    }

    public static DetailFragment newInstance (Bundle bundle){
        Bundle args = new Bundle();
        args.putBundle("bundle", bundle);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_detail, container, false);
        View rootView = mBinding.getRoot();

        mBundle = getArguments().getBundle("bundle");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvDetailActivity.setLayoutManager(layoutManager);

        mBinding.rvDetailActivity.setHasFixedSize(true);
        mAdapter = new ChannelDetailsCollectionAdapter();
        mBinding.rvDetailActivity.setAdapter(mAdapter);

        mBinding.detailNoInfo.setVisibility(View.GONE);

        if(mBundle != null){
            mChannelId  = mBundle.getString("id");
            mChannelImage  = mBundle.getString("thumbnail");
            mChannelTitle  = mBundle.getString("title");
            mChannelDes  = mBundle.getString("des");
        }

        getLoaderManager().initLoader(CHANNEL_DETAILS_LOADER_ID,null, this);
        return rootView;
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
                    URL channelsUrl = new URL(getString(R.string.details_url) + mChannelId + getString(R.string.details_key));
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
            mBinding.detailNoInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ChannelDetailsCollection> loader) {

    }
}