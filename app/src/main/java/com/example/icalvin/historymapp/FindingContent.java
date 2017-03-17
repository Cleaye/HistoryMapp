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

    public FindingContent(Context context) {
        dbInterface = new DatabaseInterface(context);
    }

    public static FindingItem createFindingItem(String id, String name, String description, String period, String imageURL) {
        return new FindingItem(id, name, description, period, imageURL);
    }

    public List<FindingItem> getFindingsFromPlace(String place) throws ExecutionException, InterruptedException {
        ITEMS = dbInterface.getFindingsFromPlace(place, 1, 100, new ArrayList<FindingItem>());
        for(FindingItem item : ITEMS) {
            ITEM_MAP.put(item.id, item);
        }
        return ITEMS;
    }

    public List<FindingItem> getFindingsByPeriod(String period) throws ExecutionException, InterruptedException {
        ITEMS = dbInterface.getFindingsByPeriod(period, 1, 100, new ArrayList<FindingItem>());
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

        public FindingItem(String id, String name, String description, String period, String imageURL) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.period = period;
            this.imageURL = imageURL;
        }

        public void getImage(Context context, ImageView view) {
            Glide.with(context).load(imageURL).into(view);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
