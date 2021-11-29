package com.fanliga.models;

public class LatestAdded {

    String latestVideoId, latestVideoPath, latestVideoThumbnail, latestVideoTags;

    public LatestAdded(String latestVideoId, String latestVideoPath, String latestVideoThumbnail, String latestVideoTags) {
        this.latestVideoId = latestVideoId;
        this.latestVideoPath = latestVideoPath;
        this.latestVideoThumbnail = latestVideoThumbnail;
        this.latestVideoTags = latestVideoTags;
    }

    public String getLatestVideoId() {
        return latestVideoId;
    }

    public void setLatestVideoId(String latestVideoId) {
        this.latestVideoId = latestVideoId;
    }

    public String getLatestVideoPath() {
        return latestVideoPath;
    }

    public void setLatestVideoPath(String latestVideoPath) {
        this.latestVideoPath = latestVideoPath;
    }

    public String getLatestVideoThumbnail() {
        return latestVideoThumbnail;
    }

    public void setLatestVideoThumbnail(String latestVideoThumbnail) {
        this.latestVideoThumbnail = latestVideoThumbnail;
    }

    public String getLatestVideoTags() {
        return latestVideoTags;
    }

    public void setLatestVideoTags(String latestVideoTags) {
        this.latestVideoTags = latestVideoTags;
    }
}
