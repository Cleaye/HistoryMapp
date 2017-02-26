package com.example.icalvin.historymapp;

import android.content.Context;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseInterface {
    private Context Context;

    public DatabaseInterface(Context context)
    {
        Context = context;
    }

    public List<LatLng> getPlaces() {
        String url = "http://62.221.199.184:5842/action=get&command=search&query=notisnullVindplaats&range=*&fields=*";

        Document doc = requestURL(url);
        List<LatLng> coordinateList = getCoordinatesFromPlaces(doc);

        return coordinateList;
    }

    public Document requestURL(String url) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new URL(url).openStream());
        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(Context, "Failed database connection!", Toast.LENGTH_SHORT).show();
        }

        return doc;
    }

    public List<LatLng> getCoordinatesFromPlaces(Document doc) {
        Geocoder geocoder = new Geocoder(Context, Locale.US);
        List<LatLng> coordinateList = new ArrayList<>();
        List<String> placesList = new ArrayList<>();
        try {
            if(placesList != null && !placesList.isEmpty()){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordinateList;
    }
}
