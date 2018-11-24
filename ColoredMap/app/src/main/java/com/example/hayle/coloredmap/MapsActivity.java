package com.example.hayle.coloredmap;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnGroundOverlayClickListener {

    private GoogleMap mMap;

    private static final int TRANSPARENCY_MAX = 100;
    private GroundOverlay mGroundOverlay;
    private GroundOverlay mGroundOverlayRotated;

    private static final LatLng CENTRAL_LOT = new LatLng(30.405803686603857, -91.17964625358582);
    private static final LatLng EAST_LOT = new LatLng(30.405118946087722,-91.17716789245605);

    //array with all the pictures in it
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public static final CameraPosition LSU =
            new CameraPosition.Builder().target(new LatLng(30.4133, -91.1800))
                    .zoom(15.5f)
                    .bearing(300)
                    .tilt(50)
                    .build();

    /**
     * Toggles the visibility between 100% and 50% when a {@link GroundOverlay} is clicked.
     */
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        // Toggle transparency value between 0.0f and 0.5f. Initial default value is 0.0f.
        mGroundOverlayRotated.setTransparency(0.5f - mGroundOverlayRotated.getTransparency());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void changeCamera(CameraUpdate update) {
        changeCamera(update);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker at LSU and move the camera
        LatLng lsu = new LatLng(30.4133, -91.1800);
        //mMap.addMarker(new MarkerOptions().position(lsu).title("Marker at LSU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTRAL_LOT, 15));
        //changeCamera(CameraUpdateFactory.newCameraPosition(lsu));

        mImages.clear();
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.bluesquare));


        //small overlay on central parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
        .image(mImages.get(0)).anchor(0,1)
        .position(CENTRAL_LOT, 300F, 300F)//size and location
        .bearing(30));
    }

}