package com.android.example.shortyoutube.classes;

import java.util.ArrayList;
import java.util.List;

public class ChannelVideosCollection {
    private List<ChannelVideos> mVideoList;

    public ChannelVideosCollection() {
        mVideoList = new ArrayList<>();
    }

    public int addChannel(ChannelVideos channelVideos) {
        mVideoList.add(channelVideos);
        return mVideoList.size();
    }

    public ChannelVideos getChannel(int location) {
        return mVideoList.get(location);
    }

    public int getChannelVideoSize() {
        return mVideoList.size();
    }

    public List<ChannelVideos> getAllItems() {
        return mVideoList;
    }

    public boolean isVideoEmpty(){
        return mVideoList.isEmpty();
    }

    public static class ChannelVideos{
        private String videoId;
        private String videoTitle;
        private String videoDes;
        private String videoUrl;
        private String videoPublishTime;

        public ChannelVideos(String id, String title, String des, String url, String time){
            this.videoId = id;
            this.videoTitle = title;
            this.videoDes = des;
            this.videoUrl = url;
            this.videoPublishTime = time;
        }

        public String getVideoId(){
            return videoId;
        }

        public String getVideoTitle(){
            return videoTitle;
        }

        public String getVideoDes(){
            return videoDes;
        }

        public String getVideoUrl(){
            return videoUrl;
        }

        public String getPublishTime(){
            return videoPublishTime;
        }
    }
}
