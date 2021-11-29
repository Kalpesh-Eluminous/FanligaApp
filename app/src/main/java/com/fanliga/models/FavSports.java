package com.fanliga.models;

public class FavSports {

    String sport_name, sport_id;

    public FavSports(String sport_name, String sport_id) {
        this.sport_name = sport_name;
        this.sport_id = sport_id;
    }

    public String getSport_name() {
        return sport_name;
    }

    public void setSport_name(String sport_name) {
        this.sport_name = sport_name;
    }

    public String getSport_id() {
        return sport_id;
    }

    public void setSport_id(String sport_id) {
        this.sport_id = sport_id;
    }
}
