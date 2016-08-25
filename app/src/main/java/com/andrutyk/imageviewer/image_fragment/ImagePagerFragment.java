package com.andrutyk.imageviewer.image_fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.andrutyk.imageviewer.R;
import com.andrutyk.imageviewer.adapters.ImagesPagerAdapter;
import com.andrutyk.imageviewer.animations.ZoomOutPageTransformer;
import com.andrutyk.imageviewer.main.FindItemsInteractorImpl;
import com.andrutyk.imageviewer.main.ImageListPresenter;
import com.andrutyk.imageviewer.main.ImageListPresenterImpl;
import com.andrutyk.imageviewer.main.ImageListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment implements ImageListView {

    private ImageListPresenter imageListPresenter;

    private ArrayList<JSONObject> data;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        imageListPresenter = new ImageListPresenterImpl(this,
                new FindItemsInteractorImpl(getActivity()));
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageListPresenter.onResume(getCategoryName(0));
    }

    @Override
    public void onDestroy() {
        imageListPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setItems(List<JSONObject> items) {
        data = (ArrayList<JSONObject>) items;
        if (pagerAdapter != null) {
            pagerAdapter = null;
        }
        pagerAdapter = new ImagesPagerAdapter(getActivity().getSupportFragmentManager(), data);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_add_to_favorite:
                imageListPresenter.setFavorite(pager.getCurrentItem(), true);
                return true;
            case R.id.item_del_from_favorite:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void setCategory(int position) {
        imageListPresenter.getDataByCategory(getCategoryName(position));
    }

    private String getCategoryName(int categoryNumber) {
        switch (categoryNumber){
            case 0:
                return FindItemsInteractorImpl.CATEGORY_ALL;
            case 1:
                return FindItemsInteractorImpl.CATEGORY_FAVORITE;
            default:
                return FindItemsInteractorImpl.CATEGORY_ALL ;
        }
    }
}
