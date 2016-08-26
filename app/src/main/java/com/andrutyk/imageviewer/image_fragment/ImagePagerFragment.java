package com.andrutyk.imageviewer.image_fragment;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.andrutyk.imageviewer.R;
import com.andrutyk.imageviewer.adapters.ImagesPagerAdapter;
import com.andrutyk.imageviewer.animations.DepthPageTransformer;
import com.andrutyk.imageviewer.animations.ZoomOutPageTransformer;
import com.andrutyk.imageviewer.main.FindItemsInteractorImpl;
import com.andrutyk.imageviewer.imagelist_mvp.ImageListPresenter;
import com.andrutyk.imageviewer.imagelist_mvp.ImageListPresenterImpl;
import com.andrutyk.imageviewer.imagelist_mvp.ImageListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment implements ImageListView {

    private final static String COMMENT_FIELD_NAME = "comment";
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
            if (position >= pagerAdapter.getCount() - 1) {
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
                showInputDialog();
                return true;
            case R.id.item_del_from_favorite:
                String comment = "";
                try {
                    comment = data.get(pager.getCurrentItem()).getString(COMMENT_FIELD_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageListPresenter.setFavorite(
                        ((ImagesPagerAdapter) pagerAdapter).getID(pager.getCurrentItem()),
                        false,
                        comment);
                refreshData();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText etComment = (EditText) promptView.findViewById(R.id.etComment);
        try {
            String comment = data.get(pager.getCurrentItem()).getString(COMMENT_FIELD_NAME);
            etComment.setText(comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getRes().getString(R.string.alert_btn_ok_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imageListPresenter.setFavorite(
                                        ((ImagesPagerAdapter) pagerAdapter).getID(pager.getCurrentItem()),
                                        true,
                                        etComment.getText().toString());
                                refreshData();
                            }
                        })
                .setNegativeButton(getRes().getString(R.string.alert_btn_cancel_text),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
        return sortOrder.equals(sortValuesArr[0]);
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
        position = pager.getCurrentItem();
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
        if (animation.isEmpty() || animation.equals(animations[0])) {
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
