package com.fanliga.models;

import java.io.Serializable;

public class Leagues implements Serializable {
    String id,league_name,is_bookmark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeague_name() {
        return league_name;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }

    public String getIs_bookmark() {
        return is_bookmark;
    }

    public void setIs_bookmark(String is_bookmark) {
        this.is_bookmark = is_bookmark;
    }
}
