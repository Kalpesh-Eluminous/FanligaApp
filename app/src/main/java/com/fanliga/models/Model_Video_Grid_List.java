package com.fanliga.models;

import java.util.ArrayList;
import java.util.List;

public class Model_Video_Grid_List {
    private String message;
    private boolean status;
    ArrayList< Object > data = new ArrayList < Object > ();
    ArrayList < Object > errors = new ArrayList < Object > ();


    // Getter Methods

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }

    // Setter Methods

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}