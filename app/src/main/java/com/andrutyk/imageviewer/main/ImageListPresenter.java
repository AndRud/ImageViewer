package com.andrutyk.imageviewer.main;

/**
 * Created by admin on 22.08.2016.
 */
public interface ImageListPresenter {

    void getDataByCategory(String category, boolean sortByOrdinal);

    void setFavorite(int position, boolean isFavorite);

    void setComment(int position, String comment);

    void onDestroy();
}