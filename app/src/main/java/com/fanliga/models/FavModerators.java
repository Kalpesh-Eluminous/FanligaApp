package com.fanliga.models;

public class FavModerators {

    String moderator_name, moderator_id;

    public FavModerators(String moderator_name, String moderator_id) {
        this.moderator_name = moderator_name;
        this.moderator_id = moderator_id;
    }

    public String getModerator_name() {
        return moderator_name;
    }

    public void setModerator_name(String moderator_name) {
        this.moderator_name = moderator_name;
    }

    public String getModerator_id() {
        return moderator_id;
    }

    public void setModerator_id(String moderator_id) {
        this.moderator_id = moderator_id;
    }
}
