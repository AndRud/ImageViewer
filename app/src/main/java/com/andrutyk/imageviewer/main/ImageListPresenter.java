package com.andrutyk.imageviewer.main;

/**
 * Created by admin on 22.08.2016.
 */
public interface ImageListPresenter {

    void onResume();

    void setFavorite(int position, boolean isFavorite);

    void onItemClicked(int position);

    void onDestroy();
}
