package com.andrutyk.imageviewer.main;

import android.content.Context;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public class ImageListPresenterImpl implements ImageListPresenter, FindItemsInteractor.OnFinishedListener{

    private final static String PROP_IS_FAVORITE_NAME = "isFavorite";

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
        findItemsInteractor.setFavorite(context, position, true);
    }

    @Override
    public void onItemClicked(int position) {
        imageListView.showMessage(String.format("Position %d clicked", position + 1));
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
