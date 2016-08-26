package com.andrutyk.imageviewer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andrutyk.imageviewer.image_fragment.PageFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 23.08.2016.
 */
public class ImagesPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<JSONObject> data;

    public ImagesPagerAdapter(FragmentManager fm, ArrayList<JSONObject> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = "";
        String comment = "";
        try {
            url = data.get(position).getString("url");
            comment = data.get(position).getString("comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return PageFragment.newInstance(url, comment);
    }

    public int getID(int position) {
        try {
            return data.get(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
