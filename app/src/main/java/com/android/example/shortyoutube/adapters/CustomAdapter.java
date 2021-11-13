package com.android.example.shortyoutube.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.fragments.DetailFragment;
import com.android.example.shortyoutube.fragments.VideoFragment;

public class CustomAdapter extends FragmentPagerAdapter {

    private Bundle mDetailBundle, mVideoBundle;
    private Context mContext;

    public CustomAdapter(Context context, FragmentManager fm, Bundle bundle1, Bundle bundle2) {
        super(fm);
        mContext = context;
        mDetailBundle = bundle1;
        mVideoBundle = bundle2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return DetailFragment.newInstance(mDetailBundle);
        }
        else {
            return VideoFragment.newInstance(mVideoBundle);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return mContext.getString(R.string.fragment_name_details);
        }
        else{
            return mContext.getString(R.string.fragment_name_videos);
        }
    }
}
