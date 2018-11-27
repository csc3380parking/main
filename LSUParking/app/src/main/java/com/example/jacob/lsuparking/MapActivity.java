package com.example.jacob.lsuparking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jacob.lsuparking.Models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnGroundOverlayClickListener {


    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 17f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136));

    private static final int TRANSPARENCY_MAX = 100;
    private GroundOverlay mGroundOverlay;
    private GroundOverlay cGroundOverlay;
    private static final LatLng CENTRAL_LOT = new LatLng(30.405803686603857, -91.17964625358582);
    private static final LatLng EAST_LOT = new LatLng(30.40510969280462,-91.17716789245605);
    private static final LatLng STADIUM_LOT_1 = new LatLng(30.41017851000956,-91.18460691642986);
    private static final LatLng STADIUM_LOT_2 = new LatLng(30.410313600862967,-91.18611539117273);
    private static final LatLng STADIUM_LOT_3 = new LatLng(30.411505924312713,-91.18607631336329);
    private static final LatLng VISITOR_LOT_2 = new LatLng(30.411505924312713,-91.18607631336329);


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    private ImageView mLsu;
    private ImageView mAdd;
    private ImageView mSub;


    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private int numParkedIncrementor = 238;

    private PlaceAutocompleteAdapter mplaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private PlaceInfo mPlace;

    //array to hold blue, green, red, pink, pics for the overlays
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    public int buttonVal =0; //0 is add, 1 is subtract
    public int lotSpaces = 5;
    public int centralAvail = 800;
    public int eastAvail= 400;
    Marker central, east;
    public String lot = null;




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready here");
        mMap = googleMap;
        mMap.setOnGroundOverlayClickListener(this);
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }

        mImages.clear();

    /*------------------------- LOT OVERLAYS -------------------------------------------------------------*/

        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.pft_central_lot));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.pft_east_lot));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.stadium_lot_1));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.visitor_lot_1));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.stadium_lot_2));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.stadium_lot_3));
        mImages.add(BitmapDescriptorFactory.fromResource(R.drawable.visitor_lot_2));

        //small overlay on central parking lot
        cGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(0)).anchor((float).48,(float).49)
                //int2 is width, int3 is height
                .position(CENTRAL_LOT, 510F, 300F)//size and location
                .bearing(0) //rotation
                .transparency((float) 0.5));
        cGroundOverlay.setTag("central");
        //small overlay on east parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(1)).anchor((float).29,(float).65)
                .position(EAST_LOT, 400F, 250F)//size and location
                .bearing(28) //rotation
                .transparency((float) 0.5)
                .clickable(true));
        mGroundOverlay.setTag("east");

        //small overlay on central parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(2)).anchor((float).5,(float).50)
                //int2 is width, int3 is height
                .position(STADIUM_LOT_1, 300F, 300F)//size and location
                .bearing(0) //rotation
                .transparency((float) 0.5));

        //small overlay on central parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(3)).anchor((float).5,(float).50)
                //int2 is width, int3 is height
                .position(STADIUM_LOT_1, 300F, 300F)//size and location
                .bearing(0) //rotation
                .transparency((float) 0.5));

        //small overlay on central parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(4)).anchor((float).5,(float).50)
                //int2 is width, int3 is height
                .position(STADIUM_LOT_2, 300F, 300F)//size and location
                .bearing(0) //rotation
                .transparency((float) 0.5));

        //small overlay on central parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(5)).anchor((float).5,(float).50)
                //int2 is width, int3 is height
                .position(STADIUM_LOT_3, 300F, 300F)//size and location
                .bearing(0) //rotation
                .transparency((float) 0.5));

        //small overlay on central parking lot
        mGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                //anchor1 is distance from top, and anchor2 is distance from left
                .image(mImages.get(6)).anchor((float).56,(float).50)
                //int2 is width, int3 is height
                .position(STADIUM_LOT_3, 300F, 300F)//size and location
                .bearing(0) //rotation
                .transparency((float) 0.5));



/*---------------------------- MAKING MARKERS -----------------------------------------------------------------------*/



        central = mMap.addMarker(new MarkerOptions()
                .position(CENTRAL_LOT).alpha(0)
                .title("Central Lot").snippet(numParkedIncrementor+"/1264"));
        east = mMap.addMarker(new MarkerOptions()
                .position(EAST_LOT).alpha(0)
                .title("East Lot").snippet(eastAvail+"/1275"));
        Marker stadium1 = mMap.addMarker(new MarkerOptions().position(new LatLng(30.410212065295177,-91.18414346887533)).alpha(0)
                    .title("Stadium Lot 1").snippet("421/853"));
        Marker stadium2 = mMap.addMarker(new MarkerOptions().position(new LatLng(30.410494090225985,-91.18559365894129)).alpha(0)
                .title("Stadium Lot 2").snippet("421/853"));
        Marker stadium3 = mMap.addMarker(new MarkerOptions().position(new LatLng(30.411841224507587,-91.1856061220169)).alpha(0)
                .title("Stadium Lot 3").snippet("101/207"));
        Marker visitor1 = mMap.addMarker(new MarkerOptions().position(new LatLng(30.41061030500111,-91.18465485861037)).alpha(0)
                .title("Visitor Lot 1").snippet("19/57"));
        Marker visitor2 = mMap.addMarker(new MarkerOptions().position(new LatLng(30.41231811306971,-91.1857849178341)).alpha(0)
                .title("Visitor Lot 2").snippet("11/51"));
    }

    /**
     * Toggles the visibility between 100% and 50% when a {@link GroundOverlay} is clicked.
     */
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {

        if (groundOverlay.getTag() == "central") {
            //lotSpaces = centralAvail;
            lot = "central";
            //alter(lotSpaces);
            groundOverlay.setTransparency(0);
            central.setSnippet(centralAvail+"/1264");
        }else if(groundOverlay.getId() == "east"){
            groundOverlay.setTransparency(0);
        }
    }

//    @SuppressLint("ResourceType")
//    public int alter(int availSpaces){
//        if(buttonVal ==0){
//            availSpaces--;
//            d.setImageResource(R.id.ic_sub);
//            buttonVal = 1;
//        }else if(buttonVal == 1){
//            availSpaces++;
//            mAdd.setImageResource(R.id.ic_add);
//            buttonVal = 0;
//        }
//        return availSpaces;
//    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        /*---------------------------------------Setting widgets and keyboard------------------------------*/
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mLsu = (ImageView) findViewById(R.id.ic_lsu);
        mAdd = (ImageView)findViewById(R.id.ic_add); //rebuild project if needed
        mSub = (ImageView)findViewById(R.id.ic_sub);

        getLocationPermision();

    }


    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        //onClick for autocomplete Results
        mSearchText.setOnItemClickListener(mAutocompleteClicklistener);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        mplaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mplaceAutocompleteAdapter);


        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();

                }
                return false;
            }
        });

        //click on the location button to move to your current location
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();

        //click on the ... button to move over LSU
        mLsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                moveToLsu();
            }
        });


        mAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick: clicked add");
//                if (lot == "central") {
//                    lotSpaces = centralAvail;
//                }
//                centralAvail = alter(lotSpaces);
//                central.setSnippet(centralAvail+"/1264");
                //setParked();
                if(buttonVal==0) {
                    numParkedIncrementor++;
                    central.setSnippet(numParkedIncrementor + "/1264");
                    buttonVal=1;
                }
            }
        });

        mSub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick: clicked sub");
//                if (lot == "central") {
//                    lotSpaces = centralAvail;
//                }
//                centralAvail = alter(lotSpaces);
//                central.setSnippet(centralAvail+"/1264");
                //setParked();
                if(buttonVal==1){
                numParkedIncrementor--;
                central.setSnippet(numParkedIncrementor+"/1264");
                }
            }
        });
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if(list.size()>0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location"+address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();


            moveCamera( new LatLng(address.getLatitude(), address.getLongitude()),
                    DEFAULT_ZOOM,address.getAddressLine(0));
            hideSoftKeyboard();

        }

    }



    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,"My Location");
                            // moves to lsu on create moveCamera(new LatLng(30.4133, -91.1800),DEFAULT_ZOOM, "My Location");
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: "+ e.getMessage());
        }
    }

    private void moveToLsu(){
        Log.d(TAG,"moveToLsu: moving camera over LSU");
        // Add a marker at LSU and move the camera
        LatLng lsu = new LatLng(30.4133, -91.1800);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lsu, 15));
        //changeCamera(CameraUpdateFactory.newCameraPosition(lsu));
    }


    private void setParked(){
        this.numParkedIncrementor++;
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to lat: " + latLng.latitude +",lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        //hides keyboard after search
        hideSoftKeyboard();

    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map;");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);

    }




    private void getLocationPermision() {
        Log.d(TAG, "getLocationpermission: getting location permisssions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }

    /*--------------- GOOGLE PLACES API AUTOCOMPLETE SUGESSTIONS ------------------*/

    private AdapterView.OnItemClickListener mAutocompleteClicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mplaceAutocompleteAdapter.getItem(position);
            final String placeID = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient,placeID);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG,"onResult: Place query did not complete successfully:" + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {


                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                //mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch(NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException:" + e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude),
                    DEFAULT_ZOOM, mPlace.getName());

            places.release();
        }
    };
}
