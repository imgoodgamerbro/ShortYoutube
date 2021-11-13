package com.android.example.shortyoutube.fragments;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.UtilsAndBackground.AppUtils;
import com.android.example.shortyoutube.UtilsAndBackground.JsonParsing;
import com.android.example.shortyoutube.adapters.ChannelVideosCollectionAdapter;
import com.android.example.shortyoutube.classes.ChannelVideosCollection;
import com.android.example.shortyoutube.databinding.FragmentVideoBinding;

import java.io.IOException;
import java.net.URL;

public class VideoFragment extends Fragment implements
        ChannelVideosCollectionAdapter.VideoItemClickListener,
        LoaderManager.LoaderCallbacks<ChannelVideosCollection> {

    private Bundle mBundle;
    private static final int VIDEOS_LOADER_ID = 1;

    private FragmentVideoBinding mBinding;
    private ChannelVideosCollectionAdapter mAdapter;

    private String mChannelId, result;

    public VideoFragment(){
        //Do nothing
    }

    public static VideoFragment newInstance(Bundle bundle){
        Bundle args = new Bundle();
        args.putBundle("bundle", bundle);
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setArguments(args);
        return videoFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_video, container, false);
        View rootView = mBinding.getRoot();

        mBundle = getArguments().getBundle("bundle");

        if(mBundle != null){
            mChannelId = mBundle.getString("id");
            result = mBundle.getString("result");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getOrientationGridSpans());
        mBinding.rvVideoActivity.setLayoutManager(layoutManager);

        mBinding.rvVideoActivity.setHasFixedSize(true);
        mAdapter = new ChannelVideosCollectionAdapter(this);
        mBinding.rvVideoActivity.setAdapter(mAdapter);
        mBinding.videoNoInfo.setVisibility(View.GONE);

        getLoaderManager().initLoader(VIDEOS_LOADER_ID, null, this);
        return rootView;
    }

    private int getOrientationGridSpans() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return 2;
        else
            return 1;
    }

    @NonNull
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ChannelVideosCollection> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ChannelVideosCollection>(requireContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mBinding.loadCircle.setVisibility(View.VISIBLE);

                Log.d("LOADER", "FETCHING NEW DATA");
                forceLoad();
            }

            @Override
            public ChannelVideosCollection loadInBackground() {
                String searchResults = null;
                try {
                    URL channelsUrl = new URL(getString(R.string.videos_url_1) + mChannelId + getString(R.string.videos_url_2)
                            + result + getString(R.string.videos_url_3));
                    searchResults = AppUtils.getResponseFromHttpUrl(channelsUrl);
                    Log.d("VideoFragment Url", searchResults);
                    if(mChannelId != null){
                        Log.d("VideoFragmentId", mChannelId);
                    } else {
                        Log.d("VideoFragmentId", "null Id");
                    }
                    return JsonParsing.parseJsonVideoData(searchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ChannelVideosCollection> loader, ChannelVideosCollection data) {
        mBinding.loadCircle.setVisibility(View.INVISIBLE);
        Log.d("LOADING", "LOAD FINISHED");

        if (data != null && !data.isVideoEmpty()) {
            mAdapter.videoAppend(data);
        } else {
            mBinding.videoNoInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ChannelVideosCollection> loader) {

    }


    @Override
    public void onVideoClick(int clickedItemIndex, String videoId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.videos_youtube_url) + videoId));
        try {
            this.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }
}