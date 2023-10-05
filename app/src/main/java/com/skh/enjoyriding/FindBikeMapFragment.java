package com.skh.enjoyriding;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.skh.enjoyriding.model.Bike;
import com.skh.enjoyriding.persistence.DBHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FindBikeMapFragment extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerClickListener{

    View v;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Button btnSelectBike;
    private MarkerOptions mLocationMaker;
    private Marker mLastSelectedMarker;

    private DBHelper dbh;
    private List<Bike> bikeList;

    public FindBikeMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_find_bike_map, container, false);

        //Instantiates the database helper
        dbh = new DBHelper(getActivity());

        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                LatLng toronto = new LatLng(43.6532, -79.3832);
                mMap.addMarker(new MarkerOptions().position(toronto).title("Maker in Toronto"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(toronto));

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                addListener();
            }
        });

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLocationMaker = new MarkerOptions();
        mLocationMaker.title("My position");
        mLocationMaker.snippet("Position from GPS");
        mLocationMaker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));

        // Check permission
        startLocationService();

        btnSelectBike = v.findViewById(R.id.btnSelectBike);
        btnSelectBike.setVisibility(View.GONE);

        // Todo To draw poly line on the map
//        makeGPSinforFromText();
        addMakersToMap();

        return v;
    }

    private void addListener() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
    }

    List<MarkerOptions> mBikeList = new ArrayList<>();

    public void addMakersToMap() {
        bikeList = getBikes();

        for (Bike bike:bikeList) {
            MarkerOptions mo = new MarkerOptions();
            mo.title(bike.getTitle());
            mo.snippet(bike.getSnippet());
            mo.position(new LatLng(bike.getLatitude(), bike.getLongitude()));
            mo.icon(vectorToBitmap(R.drawable.mybike, Color.parseColor("#000000")));
            mBikeList.add(mo);
        }
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
            }

            // Update location byt minTime
            FindBikeMapFragment.GPSListener gpsListener = new FindBikeMapFragment.GPSListener(mMap);
            long minTime = 10000;
            float minDistancd = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistancd, gpsListener);
//            Toast.makeText(getActivity(), "Request current position", Toast.LENGTH_SHORT).show();
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // Add exception for current position
        if ("My position".equals(marker.getTitle())) {
            return false;
        }
        if (mLastSelectedMarker != null) {
            mLastSelectedMarker.setIcon(vectorToBitmap(R.drawable.mybike, Color.parseColor("#000000")));
        }
        marker.setIcon(vectorToBitmap(R.drawable.mybike, Color.parseColor("#FD6A01")));
        mLastSelectedMarker = marker;
        selectButtonEnable();
        return false;
    }

    void selectButtonEnable() {
        btnSelectBike.setVisibility(View.VISIBLE);
        btnSelectBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScheduleBikeActivity.class);

                Bike selectedBike = null;
                for (Bike bike: bikeList) {
                    if (bike.getTitle().equals(mLastSelectedMarker.getTitle())) {
                        selectedBike = bike;
                    }
                }
                // Send info the actual bike that was clicked.
                intent.putExtra("BIKE", selectedBike);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d("KML", "onMyLocationButtonClick");
        LatLng curPoint = new LatLng(43.4793628171, -80.5185413361);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(curPoint));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        mLocationMaker.position(curPoint);
        mMap.addMarker(mLocationMaker);

        for (MarkerOptions mo : mBikeList) {
            mMap.addMarker(mo);
        }
        return false;
    }

    class GPSListener implements LocationListener {

        GoogleMap map;

        public GPSListener(GoogleMap mMap) {
            map = mMap;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Double latitude = location.getLatitude();
            Double longtitude = location.getLongitude();
            LatLng curPoint = new LatLng(latitude, longtitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

//            // Add image on current position
//            mLocationMaker.position(curPoint);
//            mMap.addMarker(mLocationMaker);
//
//            for (MarkerOptions mo : mBikeList) {
//                mMap.addMarker(mo);
//            }
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);
        }
    }

    private BitmapDescriptor vectorToBitmap(int id, int color) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private List<Bike> getBikes(){
        List<Bike> bikes = new ArrayList<>();

        dbh = new DBHelper(getActivity());
        Cursor cursor1 = dbh.getAllBikes();

        if(cursor1 == null){
            Toast.makeText(getActivity(), "No bike record found.", Toast.LENGTH_LONG).show();
        }else{
            cursor1.moveToFirst();

            do{
                Bike bike = new Bike(
                        cursor1.getInt(0),
                        cursor1.getDouble(1),
                        cursor1.getDouble(2),
                        cursor1.getString(3),
                        cursor1.getString(4),
                        cursor1.getInt(5)
                );
                bikes.add(bike);
            }while (cursor1.moveToNext());
        }
        cursor1.close();
        dbh.close();

        return bikes;
    }
}