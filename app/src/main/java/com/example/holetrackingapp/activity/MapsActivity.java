package com.example.holetrackingapp.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holetrackingapp.R;
import com.example.holetrackingapp.comm.HoleWorker;
import com.example.holetrackingapp.comm.LocationWorker;
import com.example.holetrackingapp.comm.TrackHolesWorker;
import com.example.holetrackingapp.comm.TrackUsersWorker;
import com.example.holetrackingapp.model.Hole;
import com.example.holetrackingapp.model.Position;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, View.OnClickListener, AddHoleFragment.OnAddHoleListener {

    private GoogleMap mMap;

    // Components
    private Button addBtn;
    private Button confirmBtn;
    private TextView warningTV;

    // Google Maps Related
    private LocationManager manager;
    private ArrayList<Marker> users;
    private ArrayList<Marker> holes;

    // Global Variables
    private String user;
    private Position currentPosition;
    private Hole closestHole;

    // Thread Workers
    private LocationWorker locationWorker;
    private TrackUsersWorker trackUsersWorker;
    private TrackHolesWorker trackHolesWorker;

    // Modal Dialog
    private AddHoleFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // View References
        addBtn = findViewById(R.id.addBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        warningTV = findViewById(R.id.warningTV);

        // Initialize global variables
        user = getIntent().getExtras().getString("user");
        users = new ArrayList<>();
        holes = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize LocationManager
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Maps Setup
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,
                2, this);

        // Initialize worker threads
        locationWorker = new LocationWorker(this);
        locationWorker.start();
        trackUsersWorker = new TrackUsersWorker(this);
        trackUsersWorker.start();
        trackHolesWorker  =new TrackHolesWorker(this);
        trackHolesWorker.start();

        // Set Button Functionality
        addBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        confirmBtn.setVisibility(View.INVISIBLE);

        // Set Initial Position
        setInitialPos();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // Stop all running threads
        locationWorker.finish();
        trackUsersWorker.finish();
        trackHolesWorker.finish();
        super.onDestroy();
    }


    @SuppressLint("MissingPermission")
    public void setInitialPos() {
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
           updateMyLocation(location);
        }
    }

    /**
     * Moves the camera to the user's current location on the map, then uses it to
     * update currentPosition.
     * @param location Obtained from LocationManager, contains Latitude and Longitude.
     */
    public void updateMyLocation(Location location){
        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
        currentPosition = new Position(user, location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateMyLocation(location);
        computedDistances();
    }

    /**
     * Calculates the distance between the current user and every hole.
     * <br><br>
     * If there are no holes, or the nearest hole is 1,000,000,000m (or a million kilometers) away,
     * then the warning area will show "No se han detectado huecos." Otherwise, it'll set the
     * warning message to show the distance to the nearest hole.
     * <br><br>
     * If the nearest is hole is less than 20m away, is not yet confirmed, and its author is a user
     * other than the current one, then the confirm button will become visible to the user.
     */
    private void computedDistances() {
        double distanceToHole = 1000000000;
        Position myPos = getCurrentPosition();
        Gson gson = new Gson();
        for(int i=0; i<holes.size(); i++){
            Marker hole = holes.get(i);
            LatLng holeLoc = hole.getPosition();
            LatLng myLoc = new LatLng(myPos.getLat(), myPos.getLng());
            double meters = SphericalUtil.computeDistanceBetween(holeLoc, myLoc);
            distanceToHole = Math.min(distanceToHole, meters);
            if(distanceToHole == meters){
                closestHole = (Hole) hole.getTag();
            }
        }

        double finalDistanceToHole = distanceToHole;
        runOnUiThread(
                () -> {
                    if(finalDistanceToHole>=1000000000){
                        warningTV.setText("No se han detectado huecos.");
                    } else {
                        warningTV.setText("Hueco a " + (int) finalDistanceToHole + " metros");
                    }
                    if(finalDistanceToHole < 20 && closestHole !=null && !closestHole.isConfirmed()
                            && !closestHole.getAuthor().equals(user)){
                        confirmBtn.setVisibility(View.VISIBLE);
                    } else {
                        confirmBtn.setVisibility(View.INVISIBLE);
                    }
                }
        );

    }

    public Position getCurrentPosition(){
        return currentPosition;
    }

    public String getUser(){
        return user;
    }

    /**
     * Clears the user markers on the map and draws new ones based on the list of Position objects
     * received. When the current user shares their name with one of the positions, their marker
     * will be GREEN, otherwise it will be AZURE.
     * @param positions List of user positions. Each consists of latitude, longitude and the
     * username.
     */
    public void updateUsers(ArrayList<Position> positions){
        runOnUiThread(
                () -> {
                    for(int i=0; i<users.size(); i++){
                        Marker m = users.get(i);
                        m.remove();
                    }

                    for(int i=0; i<positions.size(); i++){
                        Position pos = positions.get(i);
                        LatLng latLng = new LatLng(pos.getLat(), pos.getLng());
                        if(pos.getUser().equals(user)){
                            Marker m = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            users.add(m);
                        } else {
                            Marker m = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            users.add(m);
                        }
                    }
                }
        );
    }

    /**
     * Clears the hole markers on the map and draws new ones based on the list of Hole objects
     * received. The marker icon will differ depending on whether the hole has been confirmed by a
     * different user or not. Each marker has its corresponding Hole object set as its tag to later
     * retrieve for purposes of other users confirming them.
     * @param positions
     */
    public void updateHoles(ArrayList<Hole> positions){
        runOnUiThread(
                () -> {
                    for(int i=0; i<holes.size(); i++){
                        Marker m = holes.get(i);
                        m.remove();
                    }

                    for(int i=0; i<positions.size(); i++){
                        Hole hole = positions.get(i);
                        LatLng latLng = new LatLng(hole.getLat(), hole.getLng());
                        if(hole.isConfirmed()){
//                            Marker m = mMap.addMarker(new MarkerOptions().position(latLng));
                            Marker m = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.marker_hole))
                                    .anchor(0.5f,0.5f));
                            m.setTag(hole);
                            holes.add(m);
                        } else {
//                            Marker m = mMap.addMarker(new MarkerOptions().position(latLng)
//                                    .icon(BitmapDescriptorFactory
//                                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                            Marker m = mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.marker_hole_un))
                                    .anchor(0.5f,0.5f));
                            m.setTag(hole);
                            holes.add(m);
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addBtn:
                Position myPos = getCurrentPosition();
                Geocoder geocoder = new Geocoder(this);
                try {
                    Address address = geocoder.getFromLocation(myPos.getLat(), myPos.getLng(),
                            1).get(0);
                    dialog = AddHoleFragment.newInstance(myPos.getLat(), myPos.getLng(),
                            address.getAddressLine(0));
                    dialog.setListener(this);
                    dialog.show(getSupportFragmentManager(), "addHoleDialog");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.confirmBtn:
                if(closestHole != null){
                    closestHole.setConfirmed(true);
                    new HoleWorker(closestHole).start();
                    runOnUiThread(
                            () -> {
                                confirmBtn.setVisibility(View.INVISIBLE);
                            }
                    );
                }
                break;
        }
    }

    @Override
    public void onAddHole(double lat, double lng) {
        dialog.dismiss();
        Hole hole = new Hole(UUID.randomUUID().toString(), lat, lng, user);
        new HoleWorker(hole).start();
    }
}