package com.android.example.shortyoutube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.classes.ChannelVideosCollection;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChannelVideosCollectionAdapter
        extends RecyclerView.Adapter<ChannelVideosCollectionAdapter.ChannelVideosViewHolder> {

    public interface VideoItemClickListener{
        void onVideoClick(int clickedItemIndex, String videoId);
    }

    final private VideoItemClickListener mVideoItemClickListener;

    public ChannelVideosCollectionAdapter(VideoItemClickListener videoItemClickListener) {
        this.mVideoItemClickListener = videoItemClickListener;
    }

    private ChannelVideosCollection mChannelVideoCollection;

    private TextView mVideoPublish, mVideoTime, mVideoTitle, mVideoDes;
    private ImageView mVideoImage;

    @NonNull
    @Override
    public ChannelVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_video_list_item, parent, false);
        return new ChannelVideosViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelVideosViewHolder holder, int position) {
        try {
            holder.setVideoPublish(mChannelVideoCollection.getChannel(position).getPublishTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.setVideoThumbnail(mChannelVideoCollection.getChannel(position).getVideoUrl());
        holder.setVideoTitle(mChannelVideoCollection.getChannel(position).getVideoTitle());
        holder.setVideoDes(mChannelVideoCollection.getChannel(position).getVideoDes());
    }

    @Override
    public int getItemCount() {
        if(mChannelVideoCollection != null){
            return mChannelVideoCollection.getChannelVideoSize();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void videoAppend(ChannelVideosCollection channelVideosCollection){
        if(channelVideosCollection != null){
            mChannelVideoCollection = channelVideosCollection;
        }
        notifyDataSetChanged();
    }

    class ChannelVideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;

        public ChannelVideosViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            mVideoPublish = (TextView) itemView.findViewById(R.id.tv_channel_video_publish);
            mVideoTime = (TextView) itemView.findViewById(R.id.tv_channel_video_time);
            mVideoImage = (ImageView) itemView.findViewById(R.id.iv_channel_video_thumbnail);
            mVideoTitle = (TextView) itemView.findViewById(R.id.tv_channel_video_title);
            mVideoDes = (TextView) itemView.findViewById(R.id.tv_channel_video_des);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SimpleDateFormat")
        public void setVideoPublish(String videoPublish) throws ParseException {
            String publishedAt = videoPublish;

            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            readDate.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date date = readDate.parse(publishedAt);
            SimpleDateFormat writeDate = new SimpleDateFormat("dd-MM-yyyy 'T' hh:mm aa", Locale.US);
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));

            publishedAt = writeDate.format(date);

            String actualDate = "";
            String time = "";

            if (publishedAt.contains(" T ")) {
                String[] parts = publishedAt.split(" T ");
                actualDate = parts[0];
                time = parts[1];
            }

            mVideoPublish.setText(actualDate);

            setVideoTime(time);
        }

        public void setVideoTime(String time){
            mVideoTime.setText(time);
        }

        public void setVideoThumbnail(String videoThumbnail){
            Glide.with(context).load(videoThumbnail).placeholder(R.drawable.no_image).into(mVideoImage);
        }

        public void setVideoTitle(String title){
            mVideoTitle.setText(title);
        }

        public void setVideoDes(String des){
            mVideoDes.setText(des);
        }

        @Override
        public void onClick(View v) {
            int clickedItem = getAdapterPosition();
            mVideoItemClickListener.onVideoClick(clickedItem, mChannelVideoCollection.getChannel(clickedItem).getVideoId());
        }
    }
}
