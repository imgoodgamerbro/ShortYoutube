package com.android.example.shortyoutube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.classes.ChannelDetailsCollection;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ChannelDetailsCollectionAdapter extends RecyclerView.Adapter<ChannelDetailsCollectionAdapter.ChannelDetailsViewHolder> {
    private ChannelDetailsCollection mChannelDetailsCollection;

    private TextView mTitle, mDes;
    private CircularImageView mCiv;
    private TextView mTvSubs, mTvVidCnt, mTvViews;

    public ChannelDetailsCollectionAdapter(){
    }

    @NonNull
    @Override
    public ChannelDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_detail_item, parent, false);
        return new ChannelDetailsViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelDetailsViewHolder holder, int position) {
        holder.setImage(mChannelDetailsCollection.getChannel(position).getChannelDetailThumbnail());
        holder.setTitle(mChannelDetailsCollection.getChannel(position).getChannelDetailTitle());
        holder.setDes(mChannelDetailsCollection.getChannel(position).getChannelDetailDescription());

        holder.setSubscribers(mChannelDetailsCollection.getChannel(position).getChannelSubsCount());
        holder.setViews(mChannelDetailsCollection.getChannel(position).getChannelViewCount());
        holder.setVideoCounts(mChannelDetailsCollection.getChannel(position).getChannelVideoCount());
    }

    @Override
    public int getItemCount() {
        if(mChannelDetailsCollection != null){
            return mChannelDetailsCollection.getChannelSize();
        }
        else{
            return 0;
        }
    }

    public void appendData(ChannelDetailsCollection channelDetails){
        if(channelDetails != null){
            mChannelDetailsCollection = channelDetails;
        }
        notifyDataSetChanged();
    }

    public class ChannelDetailsViewHolder extends RecyclerView.ViewHolder {
        Context context;
        public ChannelDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            mCiv = (CircularImageView) itemView.findViewById(R.id.detail_circular_iv);
            mTitle = (TextView) itemView.findViewById(R.id.tv_detail_channel_title);
            mDes = (TextView) itemView.findViewById(R.id.tv_detail_channel_des);

            mTvSubs = (TextView) itemView.findViewById(R.id.tv_subscribers);
            mTvViews = (TextView) itemView.findViewById(R.id.tv_views);
            mTvVidCnt = (TextView) itemView.findViewById(R.id.tv_video_count);
        }

        public void setImage(String image){
            Glide.with(context).load(image).placeholder(R.drawable.no_image).into(mCiv);
        }

        public void setTitle(String title){
            mTitle.setText(title);
        }

        public void setDes(String des){
            mDes.setText(des);
        }

        @SuppressLint("SetTextI18n")
        public void setSubscribers(long sub){
            mTvSubs.setText(Long.toString(sub));
        }

        @SuppressLint("SetTextI18n")
        public void setViews(long views){
            mTvViews.setText(Long.toString(views));
        }

        @SuppressLint("SetTextI18n")
        public void setVideoCounts(long vidCount){
            mTvVidCnt.setText(Long.toString(vidCount));
        }
    }
}
