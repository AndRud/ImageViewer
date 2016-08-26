package com.andrutyk.imageviewer.imagelist_mvp;

import com.andrutyk.imageviewer.main.FindItemsInteractor;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by admin on 22.08.2016.
 */
public class ImageListPresenterImpl implements ImageListPresenter, FindItemsInteractor.OnFinishedListener {

    private ImageListView imageListView;
    private FindItemsInteractor findItemsInteractor;

    public ImageListPresenterImpl(ImageListView imageListView, FindItemsInteractor findItemsInteractor) {
        this.imageListView = imageListView;
        this.findItemsInteractor = findItemsInteractor;
    }

    @Override
    public void getDataByCategory(String category, boolean sortByOrdinal) {
        findItemsInteractor.findItems(category, sortByOrdinal, this);
    }

    @Override
    public void setFavorite(int position, boolean isFavorite, String comment) {
        findItemsInteractor.setFavorite(position, isFavorite, comment, this);
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
