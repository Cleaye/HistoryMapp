package com.example.icalvin.historymapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseInterface {
    private Context Context;
    public Map<String, LatLng> mDataMap;

    public DatabaseInterface(Context context) {
        Context = context;
        mDataMap = new HashMap<>();
    }

    public Map<String, LatLng> getMData() throws ExecutionException, InterruptedException {
        createMarkerURL(1, 100);
        return mDataMap;
    }

    public List<Finding> getFindingsFromPlace(String place) throws ExecutionException, InterruptedException {
        String url = "http://62.221.199.184:5842/action=get&command=search&query=and(Vindplaats="+place+";not(isnull(Image)))&range=1-100&fields=*";
        Document doc = new RequestTask().execute(url).get();
        List<Finding> findings = findingsToList(doc);

        return findings;
    }

    private void createMarkerURL(int start, int end) throws ExecutionException, InterruptedException {
        String url = "http://62.221.199.184:5842/action=get&command=search&query=and(not(isnull(Locatie));not(isnull(Image)))&range="+start+"-"+end+"&fields=*";
        Document doc = new RequestTask().execute(url).get();
        locationsToList(doc);

        NodeList countList = doc.getElementsByTagName("count");
        int count = Integer.parseInt(countList.item(0).getFirstChild().getNodeValue());
        if (count > end) {
            createMarkerURL(start + 100, end + 100);
        }
    }

    private LatLng getCoordinatesFromPlaces(String place) {
        Geocoder geocoder = new Geocoder(Context, Locale.US);
        List<Address> listOfAddress;

        try {
            listOfAddress = geocoder.getFromLocationName(place, 1);
            if(listOfAddress != null && !listOfAddress.isEmpty()){
                Address address = listOfAddress.get(0);

                double lat = address.getLatitude();
                double lon = address.getLongitude();
                return new LatLng(lat, lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void locationsToList(Document doc) {
        NodeList locationNodes = doc.getElementsByTagName("Locatie");

        for (int i = 0; i < locationNodes.getLength(); i++) {
            Node location = locationNodes.item(i);
            if (location.getNodeType() == Node.ELEMENT_NODE) {
                String[] fullPlace = location.getFirstChild().getNodeValue().split("\\\\");
                String place = fullPlace[fullPlace.length - 1] + ", " + fullPlace[fullPlace.length - 2];
                if (!mDataMap.containsKey(place)) {
                    LatLng coordinates = getCoordinatesFromPlaces(place);
                    mDataMap.put(place, coordinates);
                }
            }
        }
    }

    private List<Finding> findingsToList(Document doc) {
        NodeList findingNodes = doc.getElementsByTagName("PZHoai");
        List<Finding> findings = new ArrayList<>();

        for (int i = 0; i < findingNodes.getLength(); i++) {
            Node finding = findingNodes.item(i);
            if (finding.getNodeType() == Node.ELEMENT_NODE) {

            }
        }

        return findings;
    }

    class RequestTask extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... url) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            Document doc = null;
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new URL(url[0]).openStream());
            }
            catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(Context, "Database connection failed!", Toast.LENGTH_SHORT).show();
            }

            return doc;
        }
    }
}
