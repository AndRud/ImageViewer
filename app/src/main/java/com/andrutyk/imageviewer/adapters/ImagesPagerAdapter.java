package com.andrutyk.imageviewer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.andrutyk.imageviewer.image_fragment.PageFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 23.08.2016.
 */
public class ImagesPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<JSONObject> data;

    public ImagesPagerAdapter(FragmentManager fm, ArrayList<JSONObject> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = "";
        try {
            url = data.get(position).getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return PageFragment.newInstance(position, url);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
