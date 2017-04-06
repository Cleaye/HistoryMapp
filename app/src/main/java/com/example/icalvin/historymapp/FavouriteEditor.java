package com.example.icalvin.historymapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class used to save and edit the users favourite objects.
 */
public class FavouriteEditor {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Constructor used to set the SharedPreferences en Editor objects;
     * @param context Application context used for setting up the SharedPreferences object.
     */
    public FavouriteEditor(Context context) {
        sharedPreferences = context.getSharedPreferences("FavouritePrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Check wether an object is favourited or not.
     * @param tag Tag used to look up a certain item in SharedPreferences (now there is only 1 thing in it).
     * @param item The object that will be checked for.
     * @return Return true or false depending on if the object is in favourites.
     */
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

    /**
     * Get a list of items that are favoured.
     * @param tag What item to look for in SharedPreferences
     * @return Returns a list with all favoured items.
     */
    public List<FindingContent.FindingItem> getFavourites(String tag) {
        Gson gson = new Gson();
        List<FindingContent.FindingItem> list = new ArrayList<>();
        String jsonPrefs = sharedPreferences.getString(tag, "");

        Type type = new TypeToken<List<FindingContent.FindingItem>>() {}.getType();
        list = gson.fromJson(jsonPrefs, type);

        return list;
    }

    /**
     * Add a new item to a list of favourites.
     * @param tag Tag to define in what list the new item should be placed.
     * @param item The new item to add in a list.
     */
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

    /**
     * Remove an existing item out of a list.
     * @param tag Tag to define in what list the item should be removed.
     * @param item Item that needs to be removed.
     */
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

    /**
     * Prepares the data and saves it in SharedPreferences.
     * @param tag Name of the saved item.
     * @param value The value of the item you want to save.
     */
    private void set(String tag, String value) {
        editor.putString(tag, value);
        editor.commit();
    }
}
