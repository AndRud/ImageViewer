package com.andrutyk.imageviewer.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.andrutyk.imageviewer.R;

/**
 * Created by admin on 25.08.2016.
 */
public class ImagePreferenceFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
