package com.example.makan.activity.add_services;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.makan.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lat;
    private Double lng;
    private LatLng latLng;

    private LatLng mLatLng;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        findViewById(R.id.get_loc).setVisibility(View.INVISIBLE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SelectLocation.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.5f));
            }
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Your Service Location"));
                findViewById(R.id.get_loc).setVisibility(View.VISIBLE);

                mLatLng = latLng;
            }
        });

        findViewById(R.id.get_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Geocoder geocoder = new Geocoder(SelectLocation.this, Locale.ENGLISH);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(SelectLocation.this, add_gallery.class);

                intent.putExtra("lat", mLatLng.latitude);
                intent.putExtra("lng", mLatLng.longitude);

                intent.putExtra("city", addresses.get(0).getLocality());

                intent.putExtra("owner1", getIntent().getStringExtra("owner"));

                intent.putExtra("name1", getIntent().getStringExtra("name"));

                intent.putExtra("categories1", getIntent().getStringArrayExtra("categories"));

                intent.putExtra("periods1", getIntent().getIntegerArrayListExtra("periods"));
                intent.putExtra("prices1", getIntent().getIntegerArrayListExtra("prices"));

                intent.putExtra("601", getIntent().getStringExtra("60"));
                intent.putExtra("1201", getIntent().getStringExtra("120"));


                intent.putExtra("open1", getIntent().getStringExtra("open"));
                intent.putExtra("close1", getIntent().getStringExtra("close"));

                intent.putExtra("off1", getIntent().getStringArrayExtra("off"));


                intent.putExtra("max1", getIntent().getStringExtra("max"));
                intent.putExtra("min1", getIntent().getStringExtra("min"));
                intent.putExtra("price1", getIntent().getStringExtra("price"));
                intent.putExtra("cancel1", getIntent().getStringExtra("cancel"));

                intent.putExtra("tickets_number1", getIntent().getStringExtra("tickets_number"));

                intent.putExtra("phone1", getIntent().getStringExtra("phone"));
                intent.putExtra("email1", getIntent().getStringExtra("email"));
                intent.putExtra("description1", getIntent().getStringExtra("description"));
                intent.putExtra("image1", getIntent().getStringExtra("image"));

                intent.putExtra("serviceId", getIntent().getStringExtra("key"));

                intent.putExtra("type1", getIntent().getStringExtra("type"));
                startActivity(intent);
                finish();

            }
        });

    }
}