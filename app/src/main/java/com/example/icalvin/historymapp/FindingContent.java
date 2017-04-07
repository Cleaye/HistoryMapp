package com.example.icalvin.historymapp;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FindingContent {

    public static List<FindingItem> ITEMS = new ArrayList<FindingItem>();

    public static Map<String, FindingItem> ITEM_MAP = new HashMap<String, FindingItem>();

    private DatabaseInterface dbInterface;

    /**
     * Constructor to make a DatabaseInterface.
     * @param context Needed to make a DatabaseInterface.
     */
    public FindingContent(Context context) {
        dbInterface = new DatabaseInterface(context);
    }

    /**
     * Creates a new FindingItem, since FindingItem is a static class.
     * @param id Id of the item.
     * @param name Name of the item.
     * @param description Description of the item.
     * @param period Period in which the item belonged to.
     * @param imageURL Url to the image of the item.
     * @param coordinate Where the item was found.
     * @param finder Who found the item.
     * @return Returns a new FindingItem.
     */
    public static FindingItem createFindingItem(String id, String name, String description, String period, String imageURL, String coordinate, String finder) {
        return new FindingItem(id, name, description, period, imageURL, coordinate, finder);
    }

    /**
     * Gives a list with FindingItems found at a certain place.
     * @param place Place where the item(s) were found.
     * @return List with FindingItems.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<FindingItem> getFindingsFromPlace(String place) throws ExecutionException, InterruptedException {
        ITEMS = dbInterface.getFindingsFromPlace(place);
        for(FindingItem item : ITEMS) {
            ITEM_MAP.put(item.id, item);
        }
        return ITEMS;
    }

    /**
     * Gives a list with FindingItems found at a certain place.
     * @param period Period where the item(s) belonged to.
     * @return List with FindingItems.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<FindingItem> getFindingsByPeriod(String period) throws ExecutionException, InterruptedException {
        ITEMS = dbInterface.getFindingsByPeriod(period);
        for(FindingItem item : ITEMS) {
            ITEM_MAP.put(item.id, item);
        }
        return ITEMS;
    }

    /**
     * Gives a list with FindingItems based on a search query.
     * @param query Query given by user input.
     * @return List with FindingItems.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<FindingItem> getFindingsBySearch(String query) throws ExecutionException, InterruptedException {
        ITEMS = dbInterface.getFindingsBySearch(query);
        for(FindingItem item : ITEMS) {
            ITEM_MAP.put(item.id, item);
        }
        return ITEMS;
    }

    public static class FindingItem {
        public String id;
        public String name;
        public String description;
        public String period;
        public String imageURL;
        public String coordinate;
        public String finder;

        /**
         * Constructor for a FindingItem.
         * @param id Id of the item.
         * @param name Name of the item.
         * @param description Description of the item.
         * @param period Period in which the item belonged to.
         * @param imageURL Url to the image of the item.
         * @param coordinate Coordinate of the item.
         * @param finder Who found the object.
         */
        public FindingItem(String id, String name, String description, String period, String imageURL, String coordinate, String finder) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.period = period;
            this.imageURL = imageURL;
            this.coordinate = coordinate;
            this.finder = finder;
        }

        /**
         * Gets the image of an item from the url and puts it in an ImageView, also saves it in cache.
         * @param context Application context.
         * @param view ImageView to put the image in.
         */
        public void getImage(Context context, ImageView view) {
            Glide.with(context).load(imageURL).into(view);
        }

        /**
         * Returns the name of the item.
         * @return The name of the item.
         */
        @Override
        public String toString() {
            return name;
        }
    }
}
