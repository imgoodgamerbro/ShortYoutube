package com.android.example.shortyoutube.UtilsAndBackground;

import android.text.TextUtils;

import com.android.example.shortyoutube.classes.ChannelCollection;
import com.android.example.shortyoutube.classes.ChannelDetailsCollection;
import com.android.example.shortyoutube.classes.ChannelVideosCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParsing {

    public static ChannelCollection parseJsonData(String json) {
        ChannelCollection channelCollection;
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            channelCollection = new ChannelCollection();
            JSONArray channelJsonArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < channelJsonArray.length(); i++) {
                JSONObject currentChannel = channelJsonArray.getJSONObject(i);
                JSONObject snippet = currentChannel.getJSONObject("snippet");
                String id = snippet.getString("channelId");

                String desc = snippet.optString("description");
                if (desc.equals("")) {
                    desc = "!No Description available from Creator!";
                }

                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                JSONObject defaults = thumbnails.getJSONObject("high");
                String thumbnail = defaults.optString("url");

                String title = snippet.getString("channelTitle");
                String publish = snippet.getString("publishTime");

                channelCollection.addChannel(new ChannelCollection.Channel(
                        id, desc, thumbnail, title, publish));
            }
            return channelCollection;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ChannelDetailsCollection parseJsonChannelData(String json, String imageUrl, String title, String des) {
        ChannelDetailsCollection channelDetails;
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JSONObject baseJsonResponse = new JSONObject(json);
            channelDetails = new ChannelDetailsCollection();
            JSONArray channelJsonArray = baseJsonResponse.getJSONArray("items");

            JSONObject firstItem = channelJsonArray.getJSONObject(0);

            JSONObject statistics = firstItem.getJSONObject("statistics");


            String subs = statistics.getString("subscriberCount");
            String views = statistics.getString("viewCount");
            String videoCount = statistics.getString("videoCount");

            channelDetails.addChannel(new ChannelDetailsCollection.
                    ChannelDetails(imageUrl, title, des, Long.parseLong(subs),Long.parseLong(videoCount),Long.parseLong(views)));

            return channelDetails;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ChannelVideosCollection parseJsonVideoData(String json){
        ChannelVideosCollection videosCollection;
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try{
            JSONObject baseJsonResponse = new JSONObject(json);
            videosCollection = new ChannelVideosCollection();
            JSONArray channelVideoJsonArray = baseJsonResponse.getJSONArray("items");

            for(int i=0;i<channelVideoJsonArray.length(); i++){
                JSONObject currentVideo = channelVideoJsonArray.getJSONObject(i);

                JSONObject videoObjectId = currentVideo.getJSONObject("id");
                String id = videoObjectId.getString("videoId");

                JSONObject snippet = currentVideo.getJSONObject("snippet");

                String publish = snippet.getString("publishTime");
                String title = snippet.getString("title");
                String des = snippet.getString("description");

                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                JSONObject defaults = thumbnails.getJSONObject("high");
                String thumbnail = defaults.optString("url");

                videosCollection.addChannel(new ChannelVideosCollection.ChannelVideos(id, title, des, thumbnail, publish));

            }

            return videosCollection;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
