package com.andrutyk.imageviewer.main;

import android.content.Context;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public interface FindItemsInteractor {

    interface OnFinishedListener {
        void onFinished(List<JSONObject> items);
    }

    void findItems(String category, boolean sortByOrdinal, OnFinishedListener listener);

    void setFavorite(int id, boolean value, OnFinishedListener listener);

    void setComment(int id, String value, OnFinishedListener listener);
}