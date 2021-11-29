package com.fanliga.models;

public class HomeAdds {

    String addPosition, addPath, addUrl;

    public HomeAdds(String addPosition, String addPath, String addUrl) {
        this.addPosition = addPosition;
        this.addPath = addPath;
        this.addUrl = addUrl;
    }

    public String getAddPosition() {
        return addPosition;
    }

    public void setAddPosition(String addPosition) {
        this.addPosition = addPosition;
    }

    public String getAddPath() {
        return addPath;
    }

    public void setAddPath(String addPath) {
        this.addPath = addPath;
    }

    public String getAddUrl() {
        return addUrl;
    }

    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }
}
