package com.android.example.shortyoutube.classes;

import java.util.ArrayList;
import java.util.List;

public class ChannelDetailsCollection {
    private List<ChannelDetails> mChannelList;

    public ChannelDetailsCollection() {
        mChannelList = new ArrayList<>();
    }

    public int addChannel(ChannelDetails channelDetails) {
        mChannelList.add(channelDetails);
        return mChannelList.size();
    }

    public ChannelDetails getChannel(int location) {
        return mChannelList.get(location);
    }

    public int getChannelSize() {
        return mChannelList.size();
    }

    public List<ChannelDetails> getAllItems() {
        return mChannelList;
    }

    public static class ChannelDetails {
        private String channelDescription;
        private String channelThumbnail;
        private String channelTitle;

        private long channelSubsCount;
        private long channelVideoCount;
        private long channelViewCount;

        public ChannelDetails(String thumb, String title, String des, long subCount, long vidCount, long viewCount) {
            this.channelThumbnail = thumb;
            this.channelTitle = title;
            this.channelDescription = des;

            this.channelSubsCount = subCount;
            this.channelVideoCount = vidCount;
            this.channelViewCount = viewCount;
        }

        public String getChannelDetailThumbnail() {
            return channelThumbnail;
        }

        public String getChannelDetailTitle() {
            return channelTitle;
        }

        public String getChannelDetailDescription() {
            return channelDescription;
        }

        public long getChannelSubsCount() {
            return channelSubsCount;
        }

        public long getChannelVideoCount() {
            return channelVideoCount;
        }

        public long getChannelViewCount() {
            return channelViewCount;
        }
    }
}
