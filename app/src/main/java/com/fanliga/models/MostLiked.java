package com.fanliga.models;

public class MostLiked {

    String mostLikedVideoId, mostLikedVideoPath, mostLikedVideoThumbnail, mostLikedVideoTags;

    public MostLiked(String mostLikedVideoId, String mostLikedVideoPath, String mostLikedVideoThumbnail, String mostLikedVideoTags) {
        this.mostLikedVideoId = mostLikedVideoId;
        this.mostLikedVideoPath = mostLikedVideoPath;
        this.mostLikedVideoThumbnail = mostLikedVideoThumbnail;
        this.mostLikedVideoTags = mostLikedVideoTags;
    }

    public String getMostLikedVideoId() {
        return mostLikedVideoId;
    }

    public void setMostLikedVideoId(String mostLikedVideoId) {
        this.mostLikedVideoId = mostLikedVideoId;
    }

    public String getMostLikedVideoPath() {
        return mostLikedVideoPath;
    }

    public void setMostLikedVideoPath(String mostLikedVideoPath) {
        this.mostLikedVideoPath = mostLikedVideoPath;
    }

    public String getMostLikedVideoThumbnail() {
        return mostLikedVideoThumbnail;
    }

    public void setMostLikedVideoThumbnail(String mostLikedVideoThumbnail) {
        this.mostLikedVideoThumbnail = mostLikedVideoThumbnail;
    }

    public String getMostLikedVideoTags() {
        return mostLikedVideoTags;
    }

    public void setMostLikedVideoTags(String mostLikedVideoTags) {
        this.mostLikedVideoTags = mostLikedVideoTags;
    }
}
