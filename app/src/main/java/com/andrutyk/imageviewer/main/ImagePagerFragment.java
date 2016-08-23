package com.andrutyk.imageviewer.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrutyk.imageviewer.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment implements ImageListView{

    private ImageListPresenter imageListPresenter;

    private ArrayList<JSONObject> data;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, null);
        imageListPresenter = new ImageListPresenterImpl(getActivity(), this, new FindItemsInteractorImpl());
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageListPresenter.onResume();
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
    public void showMessage(String message) {

    }
}
