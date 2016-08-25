package com.andrutyk.imageviewer.main;

import android.content.Context;
import android.os.Handler;

import com.andrutyk.imageviewer.R;
import com.andrutyk.imageviewer.models.ImageModel;
import com.andrutyk.imageviewer.utils.JSONResourceReader;
import com.andrutyk.imageviewer.utils.PrefUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public class FindItemsInteractorImpl implements FindItemsInteractor {

    @Override
    public void findItems(Context context, OnFinishedListener listener) {
        listener.onFinished(createListFromJSON(context));
    }

    @Override
    public void setFavorite(Context context, int id, boolean value, OnFinishedListener listener) {
        JSONResourceReader resourceReader = new JSONResourceReader(context, R.raw.images);
        resourceReader.setFavorite(id, value);
        listener.onFinished(createListFromJSON(context));
    }

    @Override
    public void setComment(Context context, int id, String value, OnFinishedListener listener) {
        JSONResourceReader resourceReader = new JSONResourceReader(context, R.raw.images);
        resourceReader.setComment(id, value);
        listener.onFinished(createListFromJSON(context));
    }

    private List<JSONObject> createListFromJSON(Context context) {
        JSONResourceReader resourceReader = new JSONResourceReader(context, R.raw.images);
        ArrayList<JSONObject> images = resourceReader.getAllImagesFromJSON();
        return images;
    }
}
