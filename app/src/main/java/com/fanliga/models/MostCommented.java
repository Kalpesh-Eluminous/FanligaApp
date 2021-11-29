package com.fanliga.models;

public class MostCommented {

    String mostCommentedVideoId, mostCommentedVideoPath, mostCommentedVideoThumbnail, mostCommentedVideoTags;

    public MostCommented(String mostCommentedVideoId, String mostCommentedVideoPath, String mostCommentedVideoThumbnail, String mostCommentedVideoTags) {
        this.mostCommentedVideoId = mostCommentedVideoId;
        this.mostCommentedVideoPath = mostCommentedVideoPath;
        this.mostCommentedVideoThumbnail = mostCommentedVideoThumbnail;
        this.mostCommentedVideoTags = mostCommentedVideoTags;
    }

    public String getMostCommentedVideoId() {
        return mostCommentedVideoId;
    }

    public void setMostCommentedVideoId(String mostCommentedVideoId) {
        this.mostCommentedVideoId = mostCommentedVideoId;
    }

    public String getMostCommentedVideoPath() {
        return mostCommentedVideoPath;
    }

    public void setMostCommentedVideoPath(String mostCommentedVideoPath) {
        this.mostCommentedVideoPath = mostCommentedVideoPath;
    }

    public String getMostCommentedVideoThumbnail() {
        return mostCommentedVideoThumbnail;
    }

    public void setMostCommentedVideoThumbnail(String mostCommentedVideoThumbnail) {
        this.mostCommentedVideoThumbnail = mostCommentedVideoThumbnail;
    }

    public String getMostCommentedVideoTags() {
        return mostCommentedVideoTags;
    }

    public void setMostCommentedVideoTags(String mostCommentedVideoTags) {
        this.mostCommentedVideoTags = mostCommentedVideoTags;
    }
}
