package com.andrutyk.imageviewer.main;

import android.content.Context;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public class ImageListPresenterImpl implements ImageListPresenter, FindItemsInteractor.OnFinishedListener{

    private Context context;
    private ImageListView imageListView;
    private FindItemsInteractor findItemsInteractor;

    public ImageListPresenterImpl(Context context, ImageListView imageListView, FindItemsInteractor findItemsInteractor) {
        this.context = context;
        this.imageListView = imageListView;
        this.findItemsInteractor = findItemsInteractor;
    }

    @Override
    public void onResume() {
        findItemsInteractor.findItems(context, this);
    }

    @Override
    public void setFavorite(int position, boolean isFavorite) {
        findItemsInteractor.setFavorite(context, position, isFavorite, this);
    }

    @Override
    public void setComment(int position, String comment) {
        findItemsInteractor.setComment(context, position, comment, this);
    }

    @Override
    public void onDestroy() {
        imageListView = null;
    }

    @Override
    public void onFinished(List<JSONObject> items) {
        if (imageListView != null) {
            imageListView.setItems(items);
        }
    }
}
