package com.fanliga.models;

public class FavLeagues {

    String league_name, league_id;

    public FavLeagues(String league_name, String league_id) {
        this.league_name = league_name;
        this.league_id = league_id;
    }

    public String getLeague_name() {
        return league_name;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }

    public String getLeague_id() {
        return league_id;
    }

    public void setLeague_id(String league_id) {
        this.league_id = league_id;
    }
}
