package com.example.icalvin.historymapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.lang.reflect.Type;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM = "item";

    private FindingContent.FindingItem mItem;
    private FavouriteEditor favouriteEditor;

    private GoogleMap mMap;
    private MapView mMapView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    /**
     * Setups the View of the Fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteEditor = new FavouriteEditor(getContext());

        if (getArguments().containsKey(ARG_ITEM_ID) || getArguments().containsKey(ARG_ITEM)) {
            if (getArguments().containsKey(ARG_ITEM_ID))
                mItem = FindingContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            else {
                Gson gson = new Gson();
                Type type = new TypeToken<FindingContent.FindingItem>() {}.getType();
                mItem = gson.fromJson(getArguments().getString(ARG_ITEM), type);
            }

            final Activity activity = this.getActivity();
            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            if (collapsingToolbarLayout != null) {
                collapsingToolbarLayout.setTitle(" ");
            }

            AppBarLayout appBarLayout = (AppBarLayout)activity.findViewById(R.id.app_bar);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                /**
                 * Handles if the title must be shown or not.
                 * @param appBarLayout
                 * @param verticalOffset
                 */
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle(mItem.name);
                        isShow = true;
                    } else if(isShow) {
                        collapsingToolbarLayout.setTitle(" ");
                        isShow = false;
                    }
                }
            });

            final ImageView imageView = (ImageView) activity.findViewById(R.id.toolbar_image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPhoto(imageView);
                }
            });
            mItem.getImage(getContext(), imageView);

            LikeButton likeButton = (LikeButton) activity.findViewById(R.id.star_button);
            if(favouriteEditor.isLiked("Findings", mItem))
                likeButton.setLiked(true);
            likeButton.setOnLikeListener(new OnLikeListener() {
                /**
                 * What happens when the favourite button is pressed (while not favoured).
                 * @param likeButton The button that has been pressed.
                 */
                @Override
                public void liked(LikeButton likeButton) {
                    favouriteEditor.addToList("Findings", mItem);
                    MainActivity.updateFragment();
                }

                /**
                 * What happens when the favourite button is pressed (while favoured).
                 * @param likeButton The button that has been pressed.
                 */
                @Override
                public void unLiked(LikeButton likeButton) {
                    favouriteEditor.removeFromList("Findings", mItem);
                    MainActivity.updateFragment();
                }
            });
        }
    }

    /**
     * Fills in the details about an item.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail_text)).setText(mItem.period +"\n\n"+ mItem.name);
        }

        mMapView = (MapView) rootView.findViewById(R.id.map);
        if(mMapView != null)
        {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        return rootView;
    }

    /**
     * Loads the image in a pop-up, when clicked on.
     * @param imageView The ImageView that was clicked on.
     */
    private void loadPhoto(ImageView imageView) {
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.image_detail,
                null);
        ImageView zoomImage = (ImageView) layout.findViewById(R.id.zoom_imageview);
        zoomImage.setImageDrawable(imageView.getDrawable());
        imageDialog.setView(layout);

        imageDialog.create();
        imageDialog.show();
    }

    /**
     * Called when the MapView is finished.
     * @param gMap
     */
    @Override
    public void onMapReady(GoogleMap gMap) {
        mMap = gMap;
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mItem.coordinate != null && !mItem.coordinate.isEmpty())
            createMarkers(mMap);
    }

    /**
     * Gets locations and creates markers to pin on the map.
     * @param gMap GoogleMap to create to markers on.
     */
    private void createMarkers(GoogleMap gMap) {
        String[] coordinatesString = mItem.coordinate.split(",");
        LatLng coordinates = new LatLng(Double.parseDouble(coordinatesString[0]), Double.parseDouble(coordinatesString[1]));
        gMap.addMarker(new MarkerOptions()
                .position(coordinates));

        gMap.setOnMarkerClickListener(this);
        gMap.moveCamera( CameraUpdateFactory.newLatLngZoom(coordinates, 14.0f) );
    }

    /**
     * Called when clicked on a Marker.
     * @param marker Marker that was clicked on.
     * @return Returns true to continue this actions.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Intent intent = new Intent(getContext(), ItemListActivity.class);
        intent.putExtra("Type", "Place");
        intent.putExtra("Title", marker.getTitle());
        startActivityForResult(intent, 1);
        return true;
    }
}
