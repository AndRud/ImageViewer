package com.andrutyk.imageviewer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.andrutyk.imageviewer.main.FindItemsInteractorImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by admin on 22.08.2016.
 */
public class JSONResourceReader {

    private static final String LOGTAG = JSONResourceReader.class.getSimpleName();
    private static final String JSON_ARR_HEAD_NAME = "images";
    private static final String JSON_PROPER_IS_FAVORITE = "isFavorite";
    private static final String JSON_COMMENT = "comment";

    private String jsonString;

    private PrefUtils prefUtils;

    /**
     * Read from a resources file and create a {@link JSONResourceReader} object that will allow the creation of other
     * objects from this resource.
     *
     * @param context An application {@link Context} object.
     * @param id        The id for the resource to load, typically held in the raw/ folder.
     */
    public JSONResourceReader(Context context, int id) {
        InputStream resourceReader = context.getResources().openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
            }
        }

        jsonString = writer.toString();
        prefUtils = new PrefUtils(context);
    }

    public ArrayList<JSONObject> getImagesByCategory(String category, boolean sortByOrdinal) {
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArrayImages = jsonObject.getJSONArray(JSON_ARR_HEAD_NAME);
            for (int i = 0; i < jsonArrayImages.length(); i++) {
                JSONObject imageObject = jsonArrayImages.getJSONObject(i);
                boolean imageIsFavorite = prefUtils.isFavorite(i);
                imageObject.put(JSON_PROPER_IS_FAVORITE , imageIsFavorite);
                imageObject.put(JSON_COMMENT , prefUtils.getComment(i));
                if(category.equals(FindItemsInteractorImpl.CATEGORY_ALL)) {
                    arrayList.add(imageObject);
                } else if (category.equals(FindItemsInteractorImpl.CATEGORY_FAVORITE) && imageIsFavorite) {
                    arrayList.add(imageObject);
                }
                if (!sortByOrdinal) {
                    Collections.shuffle(arrayList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void setFavorite(int id, boolean value) {
        prefUtils.setFavorite(id, value);
    }

    public void setComment(int id, String value) {
        prefUtils.setComment(id, value);
    }
}
