package com.fanliga.models;

import java.io.Serializable;

public class HomeClub implements Serializable {
    String club_id,club_name;

    public String getClub_id() {
        return club_id;
    }

    public void setClub_id(String club_id) {
        this.club_id = club_id;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }
}
