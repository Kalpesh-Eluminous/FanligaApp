package com.fanliga.models;

public class AllComments {

    String comment, first_name, last_name, profile_image,id;
    boolean isMyComment;

    public AllComments(String comment, String first_name, String last_name, String profile_image, String id, boolean isMyComment) {
        this.comment = comment;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_image = profile_image;
        this.id = id;
        this.isMyComment = isMyComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMyComment() {
        return isMyComment;
    }

    public void setMyComment(boolean myComment) {
        isMyComment = myComment;
    }
}
