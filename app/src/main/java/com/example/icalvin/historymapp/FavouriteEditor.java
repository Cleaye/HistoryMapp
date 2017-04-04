package com.example.icalvin.historymapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FavouriteEditor {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public FavouriteEditor(Context context) {
        sharedPreferences = context.getSharedPreferences("FavouritePrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isLiked(String tag, FindingContent.FindingItem item) {
        List<FindingContent.FindingItem> list = getFavourites(tag);

        if (list != null) {
            for(FindingContent.FindingItem finding : list) {
                if(finding.id.equals(item.id))
                    return true;
            }
        }

        return false;
    }

    public List<FindingContent.FindingItem> getFavourites(String tag) {
        Gson gson = new Gson();
        List<FindingContent.FindingItem> list = new ArrayList<>();
        String jsonPrefs = sharedPreferences.getString(tag, "");

        Type type = new TypeToken<List<FindingContent.FindingItem>>() {}.getType();
        list = gson.fromJson(jsonPrefs, type);

        return list;
    }

    public void addToList(String tag, FindingContent.FindingItem item) {
        Gson gson = new Gson();
        List<FindingContent.FindingItem> list = getFavourites(tag);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(item);
        String jsonList = gson.toJson(list);

        set(tag, jsonList);
    }

    public void removeFromList(String tag, FindingContent.FindingItem item) {
        Gson gson = new Gson();
        List<FindingContent.FindingItem> list = getFavourites(tag);
        for(Iterator<FindingContent.FindingItem> iterator = list.iterator() ; iterator.hasNext();) {
            if(iterator.next().id.equals(item.id)) {
                iterator.remove();
            }
        }
        list.remove(item);
        String jsonList = gson.toJson(list);

        set(tag, jsonList);
    }

    private void set(String tag, String value) {
        editor.putString(tag, value);
        editor.commit();
    }
}
