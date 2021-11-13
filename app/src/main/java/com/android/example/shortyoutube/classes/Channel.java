package com.android.example.shortyoutube.classes;

public class Channel {

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
