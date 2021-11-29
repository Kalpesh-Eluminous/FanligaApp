package com.fanliga.models;

import java.io.Serializable;

public class Sports implements Serializable {
    String id,sport_name,is_bookmark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSport_name() {
        return sport_name;
    }

    public void setSport_name(String sport_name) {
        this.sport_name = sport_name;
    }

    public String getIs_bookmark() {
        return is_bookmark;
    }

    public void setIs_bookmark(String is_bookmark) {
        this.is_bookmark = is_bookmark;
    }
}
