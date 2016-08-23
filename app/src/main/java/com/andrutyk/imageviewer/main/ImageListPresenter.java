package com.andrutyk.imageviewer.main;

/**
 * Created by admin on 22.08.2016.
 */
public interface ImageListPresenter {

    void onResume();

    void onItemClicked(int position);

    void onDestroy();
}
