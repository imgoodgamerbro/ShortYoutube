package com.android.example.shortyoutube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.classes.ChannelCollection;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChannelCollectionAdapter extends RecyclerView.Adapter<ChannelCollectionAdapter.ChannelCollectionViewHolder> {
    final private ChannelItemClickListener mChannelItemClickListener;
    private ChannelCollection mCollectionChannel;

    private TextView mTvTitle, mTvDes, mTvPublish, mTvTime;
    private CircularImageView mCivIcon;

    public ChannelCollectionAdapter(ChannelItemClickListener clickListener) {
        mChannelItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public ChannelCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_item, parent, false);
        return new ChannelCollectionViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelCollectionViewHolder holder, int position) {
        holder.setImage(mCollectionChannel.getChannel(position).getChannelThumbnail());
        holder.setTitle(mCollectionChannel.getChannel(position).getChannelTitle());
        holder.setDescription(mCollectionChannel.getChannel(position).getChannelDescription());
        try {
            holder.setPublish(mCollectionChannel.getChannel(position).getChannelPublishTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mCollectionChannel != null) {
            return mCollectionChannel.getChannelSize();
        } else {
            return 0;
        }
    }

    // This is very imp bcs if it is not assigned the item we
    // get is going to changing when we scroll
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void appendCollection(ChannelCollection channelCollection) {
        if (mCollectionChannel != null) {
            mCollectionChannel.getAllItems().clear();
        }
        mCollectionChannel = channelCollection;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearData(){
        if(mCollectionChannel != null) {
            mCollectionChannel.clearList();
            mCollectionChannel = null;
            notifyDataSetChanged();
        }
    }

    public class ChannelCollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;

        public ChannelCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            mCivIcon = (CircularImageView) itemView.findViewById(R.id.circularImageView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_channel_title);
            mTvDes = (TextView) itemView.findViewById(R.id.tv_channel_desc);
            mTvPublish = (TextView) itemView.findViewById(R.id.tv_publish);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(this);
        }

        public void setImage(String image) {
            Glide.with(context).load(image).placeholder(R.drawable.no_image).into(mCivIcon);
        }

        public void setTitle(String title) {
            mTvTitle.setText(title);
        }

        public void setDescription(String des) {
            mTvDes.setText(des);
        }

        @SuppressLint("SimpleDateFormat")
        public void setPublish(String publish) throws ParseException {
            String publishedAt = publish;

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


            mTvPublish.setText(actualDate);

            setTime(time);
        }

        public void setTime(String time) {
            mTvTime.setText(time);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mChannelItemClickListener.onChannelClick
                    (clickedPosition,
                            mCollectionChannel.getChannel(clickedPosition).getChannelId(),
                            mCollectionChannel.getChannel(clickedPosition).getChannelThumbnail(),
                            mCollectionChannel.getChannel(clickedPosition).getChannelTitle(),
                            mCollectionChannel.getChannel(clickedPosition).getChannelDescription());
        }
    }

    public interface ChannelItemClickListener {
        void onChannelClick(int clickedItemIndex, String id, String imageUrl, String title, String des);
    }
}