package com.andrutyk.imageviewer.models;

import org.json.JSONObject;

/**
 * Don't delete. Make a partition JSON through GSON!!!
 */
public class ImageModel extends JSONObject{

    private int id;
    private int ordNum;
    private String url;
    private boolean isFavourite;

    public ImageModel() {}

    public int getId() {
        return id;
    }

    public int getOrdNum() {
        return ordNum;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrdNum(int ordNum) {
        this.ordNum = ordNum;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }
}
