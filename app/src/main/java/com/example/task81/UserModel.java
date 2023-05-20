package com.example.task81;

import java.util.ArrayList;

public class UserModel {

    String fullName;
    //ArrayList<VideoModel> videoList;

//    public UserModel(String fullName) {
//        this(fullName, new ArrayList<VideoModel>());
//        this(fullName);
//    }
//
//   public UserModel(String fullName, ArrayList<VideoModel> videoList) {
//        this.fullName = fullName;
//        this.videoList = videoList;
//    }

    public UserModel(String fullName) {
        this.fullName = fullName;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


//    public ArrayList<VideoModel> getVideoList() {
//        return videoList;
//    }
//
//    public void setVideoList(ArrayList<VideoModel> videoList) {
//        this.videoList = videoList;
//    }


}
