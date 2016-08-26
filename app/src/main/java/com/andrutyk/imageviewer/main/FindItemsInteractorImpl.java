package com.andrutyk.imageviewer.main;

import android.content.Context;
import android.os.Handler;

import com.andrutyk.imageviewer.R;
import com.andrutyk.imageviewer.utils.JSONResourceReader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public class FindItemsInteractorImpl implements FindItemsInteractor {

    public final static String CATEGORY_ALL = "all_images";
    public final static String CATEGORY_FAVORITE = "favorite";

    private Context context;

    public FindItemsInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public void findItems(String category,boolean sortByOrdinal, OnFinishedListener listener) {
        listener.onFinished(createListFromJSON(category, sortByOrdinal));
    }

    @Override
    public void setFavorite(int id, boolean value, OnFinishedListener listener) {
        JSONResourceReader resourceReader = new JSONResourceReader(context, R.raw.images);
        resourceReader.setFavorite(id, value);
    }

    @Override
    public void setComment(int id, String value, OnFinishedListener listener) {
        JSONResourceReader resourceReader = new JSONResourceReader(context, R.raw.images);
        resourceReader.setComment(id, value);
    }

    private List<JSONObject> createListFromJSON(String category, boolean sortByOrdinal) {
        JSONResourceReader resourceReader = new JSONResourceReader(context, R.raw.images);
        ArrayList<JSONObject> images = resourceReader.getImagesByCategory(category, sortByOrdinal);
        return images;
    }
}
