package com.andrutyk.imageviewer.preference;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andrutyk.imageviewer.R;

public class ImagePreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preference);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ImagePreferenceFragment()).commit();
    }
}
