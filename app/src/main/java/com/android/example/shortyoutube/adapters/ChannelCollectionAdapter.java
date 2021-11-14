package com.android.example.shortyoutube.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.example.shortyoutube.R;
import com.android.example.shortyoutube.classes.Channel;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChannelCollectionAdapter extends ArrayAdapter<Channel> {
    private Context mContext;
    private ChannelItemClickListener mChannelItemClickListener;

    public interface ChannelItemClickListener {
        void onChannelClick(int clickedItemIndex, String id, String imageUrl, String title, String des);
    }

    public ChannelCollectionAdapter(@NonNull Activity context, ArrayList<Channel> channels, ChannelItemClickListener listener) {
        super(context, 0, channels);
        mContext = context;
        this.mChannelItemClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.channel_list_item, parent, false);
        }

        Channel currentChannel = getItem(position);
        RelativeLayout relativeLayout = (RelativeLayout) listItemView.findViewById(R.id.list_rl_1);
        CircularImageView circularImageView = (CircularImageView) listItemView.findViewById(R.id.circularImageView);
        TextView title = (TextView) listItemView.findViewById(R.id.tv_channel_title);
        TextView des = (TextView) listItemView.findViewById(R.id.tv_channel_desc);
        TextView pub = (TextView) listItemView.findViewById(R.id.tv_publish);
        TextView time = (TextView) listItemView.findViewById(R.id.tv_time);

        Glide.with(mContext).load(currentChannel.getChannelThumbnail()).placeholder(R.drawable.no_image).into(circularImageView);
        title.setText(currentChannel.getChannelTitle());
        des.setText(currentChannel.getChannelDescription());

        String publishedAt = currentChannel.getChannelPublishTime();

        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        readDate.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date date = null;
        try {
            date = readDate.parse(publishedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat writeDate = new SimpleDateFormat("dd-MM-yyyy 'T' hh:mm aa", Locale.US);
        writeDate.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));

        publishedAt = writeDate.format(date);

        String actualDate = "";
        String actualTime = "";

        if (publishedAt.contains(" T ")) {
            String[] parts = publishedAt.split(" T ");
            actualDate = parts[0];
            actualTime = parts[1];
        }

        pub.setText(actualDate);
        time.setText(actualTime);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChannelItemClickListener.onChannelClick(position,
                currentChannel.getChannelId(),
                currentChannel.getChannelThumbnail(),
                currentChannel.getChannelTitle(),
                currentChannel.getChannelDescription());
            }
        });

        return listItemView;
    }
}