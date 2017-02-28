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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseInterface {
    private Context Context;

    public DatabaseInterface(Context context)
    {
        Context = context;
    }

    public List<LatLng> getPlaces() throws ExecutionException, InterruptedException {
        String url = "http://62.221.199.184:5842/action=get&command=search&query=and(not(isnull(Vindplaats));not(isnull(Image)))&range=1-100&fields=*";

        Document doc = new RequestTask().execute(url).get();
        List<String> placesList = xmlToList(doc);
        List<LatLng> coordinateList = getCoordinatesFromPlaces(placesList);

        return coordinateList;
    }

    public List<LatLng> getCoordinatesFromPlaces(List<String> placesList) {
        Geocoder geocoder = new Geocoder(Context, Locale.US);
        List<LatLng> coordinateList = new ArrayList<>();
        List<Address> listOfAddress;

        try {
            if(placesList != null && !placesList.isEmpty()){
                for(String place : placesList) {
                    listOfAddress = geocoder.getFromLocationName(place, 1);
                    if(listOfAddress != null && !listOfAddress.isEmpty()){
                        Address address = listOfAddress.get(0);

                        double lat = address.getLatitude();
                        double lon = address.getLongitude();
                        coordinateList.add(new LatLng(lat, lon));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordinateList;
    }

    private List<String> xmlToList(Document doc) {
        NodeList nodeList = doc.getElementsByTagName("Vindplaats");
        List<String> placesList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                placesList.add(node.getFirstChild().getNodeValue());
            }
        }

        return placesList;
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
