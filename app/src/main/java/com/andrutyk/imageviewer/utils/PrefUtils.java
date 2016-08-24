package com.andrutyk.imageviewer.utils;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 24.08.2016.
 */
public class PrefUtils {

    private static final String PREF_FILE_DEFAULT = "com.andrutyk.imageviewer.utils.default";
    private static final String PREF_IS_FAVORITE = "isFavorite";
    private static final String PREF_COMMENT = "comment";

    private final Context context;

    private final SharedPreferences prefs;
    private Editor editor;

    /**
     * Manages everything relative to preferences
     *
     * @param c
     */
    public PrefUtils(Context c) {
        context = c;
        prefs = context.getSharedPreferences(PREF_FILE_DEFAULT,
                Context.MODE_PRIVATE);
    }

    Editor editor() {
        if (editor == null) {
            editor = prefs.edit();
        }
        return editor;
    }

    public void setFavorite(int id, boolean isFavorite) {
        putBoolean(PREF_IS_FAVORITE + String.valueOf(id), isFavorite);
        apply();
    }

    public boolean isFavorite(int id) {
        return getBoolean(PREF_IS_FAVORITE + String.valueOf(id), false);
    }

    public void setComment(int id, String value) {
        putBoolean(PREF_COMMENT + String.valueOf(id), value);
        apply();
    }

    public String getComment(int id) {
        return getString(PREF_COMMENT + String.valueOf(id), "");
    }

    private Editor putString(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "No resource matched key resource id");
        }
        Log.d("", "putting (key=" + key + ",value=" + value + ")");
        final Editor editor = editor();
        if (value instanceof String)
            editor.putString(key, (String) value);
        else
            throw new IllegalArgumentException("Unknown data type");
        return editor;
    }

    /**
     * Get a string preference
     *
     * @param key
     * @param defValue
     *            The string to return if the preference was not set
     * @return
     */
    private String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    private Editor putBoolean(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "No resource matched key resource id");
        }
        Log.d("", "putting (key=" + key + ",value=" + value + ")");
        final Editor editor = editor();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
            editor.commit();
        } else {
            throw new IllegalArgumentException("Unknown data type");
        }
        return editor;
    }

    /**
     *
     * @param key
     * @param defValue
     *
     * @return
     */
    private boolean getBoolean(String key, boolean defValue) {
        final Boolean result = getBooleanOrNull(key);
        return result != null ? result : defValue;
    }

    private Boolean getBooleanOrNull(String key) {
        return (prefs.contains(key)) ? prefs.getBoolean(key, false) : null;
    }

    /**
     * After applying, call {@link #editor()} again.
     */
    private void apply() {
        apply(editor());
        editor = null;
    }

    @SuppressLint("NewApi")
    private static void apply(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }
}
