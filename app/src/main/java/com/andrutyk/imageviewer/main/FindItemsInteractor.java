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

    void findItems(Context context, OnFinishedListener listener);

    void setFavorite(Context context, int id, boolean value);

    void setComment(Context context, int id, String value);
}