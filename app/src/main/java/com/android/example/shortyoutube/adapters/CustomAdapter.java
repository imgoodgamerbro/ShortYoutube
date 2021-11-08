package com.android.example.shortyoutube.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.example.shortyoutube.fragments.DetailFragment;
import com.android.example.shortyoutube.fragments.VideoFragment;

public class CustomAdapter extends FragmentPagerAdapter {

    private Bundle mDetailBundle, mVideoBundle;

    //private Context mContext;
    public CustomAdapter(FragmentManager fm, Bundle bundle1, Bundle bundle2) {
        super(fm);
        mDetailBundle = bundle1;
        mVideoBundle = bundle2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new DetailFragment(mDetailBundle);
        }
        else {
            return new VideoFragment(mVideoBundle);
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
            return "Details";
        }
        else{
            return "Videos";
        }
    }
}
