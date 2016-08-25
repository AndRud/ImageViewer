package com.andrutyk.imageviewer.main;

import android.content.Context;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public class ImageListPresenterImpl implements ImageListPresenter, FindItemsInteractor.OnFinishedListener{

    private ImageListView imageListView;
    private FindItemsInteractor findItemsInteractor;

    public ImageListPresenterImpl(ImageListView imageListView, FindItemsInteractor findItemsInteractor) {
        this.imageListView = imageListView;
        this.findItemsInteractor = findItemsInteractor;
    }

    @Override
    public void onResume(String category) {
        findItemsInteractor.findItems(category, this);
    }

    @Override
    public void getDataByCategory(String category) {
        findItemsInteractor.findItems(category, this);
    }

    @Override
    public void setFavorite(int position, boolean isFavorite) {
        findItemsInteractor.setFavorite(position, isFavorite, this);
    }

    @Override
    public void setComment(int position, String comment) {
        findItemsInteractor.setComment(position, comment, this);
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
