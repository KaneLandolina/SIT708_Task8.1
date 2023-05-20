package com.example.task81;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class YouTubeSearchResults {

    @SerializedName("items")
    private List<VideoItem> items;

    public List<VideoItem> getItems() {
        return items;
    }

    public static class VideoItem {
        @SerializedName("id")
        private VideoId videoId;

        @SerializedName("snippet")
        private VideoSnippet snippet;

        public VideoId getVideoId() {
            return videoId;
        }

        public VideoSnippet getSnippet() {
            return snippet;
        }

        public static class VideoId {

            @SerializedName("videoId")
            private String videoId;

            public String getVideoId() {
                return videoId;
            }

        }

        public static class VideoSnippet {
            @SerializedName("title")
            private String title;

            @SerializedName("channelTitle")
            private String channelTitle;

            //@SerializedName("thumbnails")
            //private String thumbnails;

            @SerializedName("thumbnails")
            private Thumbnails thumbnails;

            public String getTitle() {
                return title;
            }

            public String getChannelTitle() {
                return channelTitle;
            }

            public Thumbnails getThumbnails() {
                return thumbnails;
            }

            //public String getThumbnails() { return thumbnails; };

            public static class Thumbnails {

                @SerializedName("medium")
                private Medium medium;

                public Medium getMedium() { return medium; }

                public static class Medium {

                    @SerializedName("url")
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                }

            }


        }

    }

}
