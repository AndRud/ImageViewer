package com.andrutyk.imageviewer.image_fragment;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.andrutyk.imageviewer.R;
import com.andrutyk.imageviewer.adapters.ImagesPagerAdapter;
import com.andrutyk.imageviewer.animations.DepthPageTransformer;
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

    private final static int DEF_TIME_TO_SLIDE = 5;

    private ImageListPresenter imageListPresenter;
    private int categoryNumber;

    private ArrayList<JSONObject> data;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private int position;

    private SharedPreferences sharedPreferences;
    private int timeToSlide;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (position >= pagerAdapter.getCount() -1) {
                position = 0;
            } else {
                position = position + 1;
            }
            pager.setCurrentItem(position, true);
            handler.postDelayed(runnable, timeToSlide);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, null);
        imageListPresenter = new ImageListPresenterImpl(this,
                new FindItemsInteractorImpl(getActivity()));
        pager = (ViewPager) view.findViewById(R.id.pager);
        position = pager.getCurrentItem();
        return view;
    }

    @Override
    public void onPause() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        imageListPresenter.getDataByCategory(getCategoryName(categoryNumber), sortByOrdinal());
        setAutoSlide();
        setAnimation();
    }

    @Override
    public void onDestroy() {
        imageListPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setItems(List<JSONObject> items) {
        data = (ArrayList<JSONObject>) items;
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
                imageListPresenter.setFavorite(pager.getCurrentItem(), false);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void setCategory(int categoryNumber) {
        this.categoryNumber = categoryNumber;
        refreshData();
    }

    private void refreshData() {
        imageListPresenter.getDataByCategory(getCategoryName(categoryNumber), sortByOrdinal());
    }

    private boolean sortByOrdinal() {
        String[] sortValuesArr = getRes().getStringArray(R.array.order_values);
        String sortOrder = sharedPreferences.getString(
                getRes().getString(R.string.order_key),
                sortValuesArr[0]);
        if (sortOrder.equals(sortValuesArr[0])) {
            return true;
        } else {
            return false;
        }
    }

    private String getCategoryName(int categoryNumber) {
        switch (categoryNumber) {
            case 0:
                return FindItemsInteractorImpl.CATEGORY_ALL;
            case 1:
                return FindItemsInteractorImpl.CATEGORY_FAVORITE;
            default:
                return FindItemsInteractorImpl.CATEGORY_ALL;
        }
    }

    private void setAutoSlide() {
        boolean autoSlide = sharedPreferences.getBoolean(
                getRes().getString(R.string.turn_auto_key),
                true);
        if (autoSlide && pagerAdapter.getCount() > 1) {
            timeToSlide = Integer.valueOf(sharedPreferences.getString(
                    getRes().getString(R.string.time_to_slide_key),
                    String.valueOf(DEF_TIME_TO_SLIDE))) * 1000;
            handler.postDelayed(runnable, timeToSlide);
        }
    }

    private void setAnimation() {
        String animation = sharedPreferences.getString(
                getRes().getString(R.string.anim_key),
                getRes().getString(R.string.anim_def_value));

        String[] animations = getRes().getStringArray(R.array.anim_values);
        if (animation == null || animation.isEmpty() || animation.equals(animations[0])) {
            pager.setPageTransformer(true, null);
        } else if (animation.equals(animations[1])) {
            pager.setPageTransformer(true, new ZoomOutPageTransformer());
        } else if (animation.equals(animations[2])) {
            pager.setPageTransformer(true, new DepthPageTransformer());
        }
    }

    private Resources getRes() {
        return getActivity().getResources();
    }
}
