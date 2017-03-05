package com.example.icalvin.historymapp;

import com.google.android.gms.maps.model.LatLng;

public class MarkerTable {
    public String ID;
    public String Place;
    public LatLng Coordinates;

    public MarkerTable(String id, String place, LatLng coordinates) {
        ID = id;
        Place = place;
        Coordinates = coordinates;
    }
}
