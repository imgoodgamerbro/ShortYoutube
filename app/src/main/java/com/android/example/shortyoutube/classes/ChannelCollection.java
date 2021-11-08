package com.android.example.shortyoutube.classes;

import java.util.ArrayList;
import java.util.List;

public class ChannelCollection {

    private List<Channel> mChannelList;

    public ChannelCollection() {
        mChannelList = new ArrayList<>();
    }

    public int addChannel(Channel channel) {
        mChannelList.add(channel);
        return mChannelList.size();
    }

    public Channel getChannel(int location) {
        return mChannelList.get(location);
    }

    public int getChannelSize() {
        return mChannelList.size();
    }

    public List<Channel> getAllItems() {
        return mChannelList;
    }

    public boolean isEmpty(){
        return mChannelList.isEmpty();
    }

    public void clearList(){
        mChannelList.clear();
    }

    public static class Channel {

        private String channelId;
        private String channelDescription;
        private String channelThumbnail;
        private String channelTitle;
        private String channelPublishTime;

        public Channel(String id, String description, String thumbnail, String title, String publishTime) {
            this.channelId = id;
            this.channelDescription = description;
            this.channelThumbnail = thumbnail;
            this.channelTitle = title;
            this.channelPublishTime = publishTime;
        }

        public String getChannelId() {
            return channelId;
        }

        public String getChannelDescription() {
            return channelDescription;
        }

        public String getChannelThumbnail() {
            return channelThumbnail;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public String getChannelPublishTime() {
            return channelPublishTime;
        }

    }

}
