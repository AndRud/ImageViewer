package com.andrutyk.imageviewer.imagelist_mvp;

/**
 * Created by admin on 22.08.2016.
 */
public interface ImageListPresenter {

    void getDataByCategory(String category, boolean sortByOrdinal);

    void setFavorite(int position, boolean isFavorite, String comment);

    void onDestroy();
}