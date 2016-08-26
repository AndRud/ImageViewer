package com.andrutyk.imageviewer.imagelist_mvp;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public interface ImageListView {
    void setItems(List<JSONObject> items);
}
