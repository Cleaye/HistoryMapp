package com.example.icalvin.historymapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ThirdFragment extends Fragment implements
        IFragment, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapView mMapView;
    View mView;


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mGoogleMap;
    Context context;

    DatabaseInterface dbInterface;

    ImageButton fab;
    List<LatLng> markers = new ArrayList<LatLng>();

    /**
     * Constructor to create a new ThirdFragment
     * @param c Application context.
     */
    public ThirdFragment(Context c) {
        context = c;
    }

    /**
     * Creates a new DatabaseInterface.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbInterface = new DatabaseInterface(context);
    }

    /**
     * Gets the View of this Fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Returns the View of this Fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment, container, false);
        return mView;
    }

    /**
     * Fills in the View.
     * @param view View to setup.
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        fab = (ImageButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOnMarkers(mGoogleMap,markers, 50);
            }
        });

    }

    /**
     * Called when GoogleMap is setup. Adds users position and markers to the map.
     * @param gMap
     */
    @Override
    public void onMapReady(GoogleMap gMap) {
        mGoogleMap = gMap;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        gMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        if (ConnectionManager.hasNetworkConnection(context))
            createMarkers(gMap);
        else
            Toast.makeText(context, "Schakel een internetverbinding in", Toast.LENGTH_LONG).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * If connected request location of the user.
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context, "Verbinding verbroken...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context, "Verbinding mislukt...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when users location changed. Zooms in on users location.
     * @param location Location of the user.
     */
    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //zoom to current position:
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        //If you only need one location, unregister the listener
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Gets locations and creates markers to pin on the map.
     * @param gMap GoogleMap to create to markers on.
     */
    private void createMarkers(GoogleMap gMap) {
        try {
            Map<String, LatLng> mData = dbInterface.getLocations();
            if (mData != null && !mData.isEmpty()) {
                for (Map.Entry<String, LatLng> entry : mData.entrySet()) {
                    if(entry.getValue() != null) {
                        gMap.addMarker(new MarkerOptions()
                                .position(entry.getValue())
                                .title(entry.getKey()));
                        markers.add(entry.getValue());
                    }
                }
                gMap.setOnMarkerClickListener(this);
                zoomOnMarkers(gMap, markers, 50);
            } else {
                Toast.makeText(context, "Verbinding mislukt...", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Oeps, er ging iets fout... Check uw internet verbinding", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when clicked on a Marker.
     * @param marker Marker that was clicked on.
     * @return Returns true to continue this actions.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Intent intent = new Intent(context, ItemListActivity.class);
        intent.putExtra("Type", "Place");
        intent.putExtra("Title", marker.getTitle());
        startActivityForResult(intent, 1);
        return true;
    }

    /**
     * Zooms on all Markers, so all Markers fit in de view of the GoogleMap
     * @param gMap GoogleMap to zoom on.
     * @param markers Markers to fit inside the view.
     * @param border Offset from edges of the GoogleMap in pixels.
     */
    public void zoomOnMarkers(GoogleMap gMap, List<LatLng> markers, int border){
        if (!markers.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng marker : markers) {
                builder.include(marker);
            }
            LatLngBounds bounds = builder.build();

            int padding = border;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            gMap.animateCamera(cu);
        }
    }

    @Override
    public boolean update() {
        return false;
    }
}

