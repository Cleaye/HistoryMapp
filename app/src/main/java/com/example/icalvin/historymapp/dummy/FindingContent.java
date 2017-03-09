package com.example.icalvin.historymapp.dummy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.icalvin.historymapp.DatabaseInterface;

import java.io.InputStream;
import java.net.URL;
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

    public List<FindingItem> getFindings(String place) throws ExecutionException, InterruptedException {
        ITEMS = dbInterface.getFindingsFromPlace(place);
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

        public Bitmap getImage() throws ExecutionException, InterruptedException {
            Bitmap bitmap = new RequestImageTask().execute(imageURL).get();

            return bitmap;
        }

        @Override
        public String toString() {
            return name;
        }

        class RequestImageTask extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... url) {
                Bitmap bitmap = null;

                try {
                    InputStream is = (InputStream) new URL(url[0]).getContent();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return bitmap;
            }
        }
    }
}
