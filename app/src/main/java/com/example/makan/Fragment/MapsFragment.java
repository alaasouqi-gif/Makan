package com.example.makan.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.makan.R;
import com.example.makan.activity.item.item_Event;
import com.example.makan.activity.item.item_pitch_gym;
import com.example.makan.activity.item.item_restaurant_stores;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    static MapsFragment MapsFragment;

    public static MapsFragment getInstance() {
        return MapsFragment;
    }

    private GoogleMap mMap;

    public DatabaseReference serRef = FirebaseDatabase.getInstance().getReference().child("services");
    public DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

    public FusedLocationProviderClient mFusedLocationProviderClient;

    CircleOptions circleOptions = new CircleOptions();

    MarkerOptions markerOptions = new MarkerOptions();

    ArrayList<String> places = new ArrayList<>();

    AutoCompleteTextView placesSearch;

    Boolean placeB = false;

    String searchString;

    public static FirebaseUser firebaseUser;

    static String usersCity = "Amman";

    Double lat = 0.0;
    Double lng = 0.0;

    String updateTest = "All";

    private static final int REQUST_CODE = 123;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // Toast.makeText(getContext(), , Toast.LENGTH_SHORT).show();
            mMap = googleMap;
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsFragment.getContext());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "permission issue", Toast.LENGTH_SHORT).show();
                return;
            }
            mMap.setMyLocationEnabled(true);
            initSearch();
            moveCamera();
            updateUI();

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker1) {

                    serRef.child(marker1.getSnippet()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheet);
                            final View BottomSheet = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_marker, null);

                            TextView name_bottom = BottomSheet.findViewById(R.id.name_bottom);
                            ImageView image = BottomSheet.findViewById(R.id.image_bottom);
                            TextView details_bottom = BottomSheet.findViewById(R.id.details_bottom);
                            TextView start_date = BottomSheet.findViewById(R.id.start_date);
                            TextView end_date = BottomSheet.findViewById(R.id.end_date);
                            TextView rate = BottomSheet.findViewById(R.id.rate);
                            TextView price = BottomSheet.findViewById(R.id.price);

                            final Intent[] intent = new Intent[1];
                            BottomSheet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (dataSnapshot.child("type").getValue().toString()) {
                                        case "restaurant":
                                        case "store":
                                            intent[0] = new Intent(getContext(), item_restaurant_stores.class);
                                            intent[0].putExtra("id", marker1.getSnippet());
                                            intent[0].putExtra("type", dataSnapshot.child("type").getValue().toString());
                                            intent[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getContext().startActivity(intent[0]);
                                            break;
                                        case "event":
                                            intent[0] = new Intent(getContext(), item_Event.class);
                                            intent[0].putExtra("id", marker1.getSnippet());
                                            intent[0].putExtra("type", dataSnapshot.child("type").getValue().toString());
                                            intent[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getContext().startActivity(intent[0]);
                                            break;
                                        case "pitch":
                                        case "gym":
                                            intent[0] = new Intent(getContext(), item_pitch_gym.class);
                                            intent[0].putExtra("id", marker1.getSnippet());
                                            intent[0].putExtra("type", dataSnapshot.child("type").getValue().toString());
                                            intent[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getContext().startActivity(intent[0]);
                                            break;

                                    }

                                }
                            });


                            if ("event".equals(dataSnapshot.child("type").getValue().toString())) {
                                name_bottom.setText(marker1.getTitle());
                                details_bottom.setText(dataSnapshot.child("des").getValue().toString());
                                start_date.setText("Start Date : " + dataSnapshot.child("open").getValue().toString());
                                end_date.setText("End Date : " + dataSnapshot.child("close").getValue().toString());
                                rate.setText(dataSnapshot.child("rate").getValue().toString().substring(0, Math.min(4, dataSnapshot.child("rate").getValue().toString().length())) + " based on " + (Integer.parseInt(dataSnapshot.child("like").getValue().toString()) + Integer.parseInt(dataSnapshot.child("dislike").getValue().toString())) + " Review");
                                price.setText(dataSnapshot.child("price").getValue() + " JD ");
                                Picasso.get().load(dataSnapshot.child("cover photo").getValue().toString()).into(image);

                            } else {

                                name_bottom.setText(marker1.getTitle());
                                details_bottom.setText(dataSnapshot.child("des").getValue().toString());
                                start_date.setText("Open : " + dataSnapshot.child("open").getValue().toString());
                                end_date.setText("Close : " + dataSnapshot.child("close").getValue().toString());
                                rate.setText(dataSnapshot.child("rate").getValue().toString().substring(0, Math.min(4, dataSnapshot.child("rate").getValue().toString().length())) + " based on " + (Integer.parseInt(dataSnapshot.child("like").getValue().toString()) + Integer.parseInt(dataSnapshot.child("dislike").getValue().toString())) + " Review");
                                Picasso.get().load(dataSnapshot.child("cover photo").getValue().toString()).into(image);
                                price.setVisibility(View.GONE);
                            }

                            bottomSheetDialog.setContentView(BottomSheet);
                            bottomSheetDialog.show();

                            marker1.hideInfoWindow();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
            });

            if (!isLocationServiceRunning()) {
                Intent intent = new Intent(getActivity(), GeofireService.class);
                getActivity().startService(intent);
            }

            if (!isNotificationServiceRunning()) {
                Intent intent = new Intent(getContext(), NotificationService.class);
                getActivity().startService(intent);
            }
        }
    };

    ChipGroup chipGroup;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_maps2, container, false);
        MapsFragment = this;


        //Toast.makeText(getContext(), "created", Toast.LENGTH_SHORT).show();

        placesSearch = root.findViewById(R.id.search_bar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        TextInputLayout textInputLayout = root.findViewById(R.id.s);


        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCamera();
            }
        });

        serRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                places.clear();
                setSearchAdapter();
                for (DataSnapshot place : snapshot.getChildren()) {
                    try {
                        places.add(place.child("name").getValue().toString());
                    } catch (NullPointerException e) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error reading places", Toast.LENGTH_SHORT).show();
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersCity = snapshot.child(firebaseUser.getUid()).child("City").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Snapshot Error", Toast.LENGTH_SHORT).show();
            }
        });

        chipGroup = root.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {
                    if (chip.getText().toString().equals(" All")) {
                        updateTest = "All";
                        updateUI();

                    } else {
                        updateTest = chip.getText().toString().toLowerCase();
                        updateUI();
                    }

                } else {
                    updateTest = "All";
                    updateUI();

                }
            }
        });

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initSearch() {

        placesSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER) {
                    geoLocate();
                }

                return false;
            }
        });

        placesSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                geoLocate();
            }
        });

    }


    private void geoLocate() {

        placesSearch.clearFocus();

        placeB = false;

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(placesSearch.getWindowToken(), 0);

        searchString = placesSearch.getText().toString();

        if (searchString.equals("")) {
            Log.d("Search problem", "geoLocate: search string is null ");
        } else {
            serRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot placeLoc : snapshot.getChildren()) {
                        try {
                            if (placeLoc.child("name").getValue().toString().equals(searchString)) {
                                Log.d("Search problem", "geoLocate: no problem ");
                                //updateTest=placeLoc.child("type").getValue().toString();
                                updateTest = "All";
                                chipGroup.check(R.id.all_chip);
                                updateUI();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((Double) placeLoc.child("lat").getValue(), (Double) placeLoc.child("lng").getValue()), 15.5f));
                                placeB = true;
                                break;
                            } else {
                                Log.d("Search problem", "geoLocate: strings not equal ");
                            }
                        } catch (NullPointerException e) {
                            Log.d("Search problem", "geoLocate: catch problem ");
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            if (!placeB) {
                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addressList = new ArrayList<>();
                try {
                    addressList = geocoder.getFromLocationName(searchString, 1);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (addressList.size() > 0) {
                    Address address = addressList.get(0);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 13f));
                }
            }
        }

    }

    public void updateUI() {

        serRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();

                for (DataSnapshot loc : dataSnapshot.getChildren()) {

                    try {
                        lat = ((Double) loc.child("lat").getValue());
                        lng = ((Double) loc.child("lng").getValue());

                        circleOptions.center(new LatLng(lat, lng));
                        circleOptions.radius(500);

                        markerOptions.position(new LatLng(lat, lng))
                                .title(loc.child("name").getValue().toString())
                                .snippet(loc.getKey());

                        BitmapDescriptor markerIcon = vectorToBitmap(R.drawable.ic_fast_food);

                        switch (loc.child("type").getValue().toString()) {
                            case "restaurant":

                                circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
                                circleOptions.fillColor(Color.argb(64, 255, 0, 0));
                                markerOptions.icon(markerIcon);

                                break;
                            case "pitch":

                                circleOptions.strokeColor(Color.argb(255, 0, 255, 0));
                                circleOptions.fillColor(Color.argb(64, 0, 255, 0));
                                markerOptions.icon(vectorToBitmap(R.drawable.ic_pitch));
                                break;

                            case "event":
                                circleOptions.strokeColor(Color.argb(255, 0, 0, 255));
                                circleOptions.fillColor(Color.argb(64, 0, 0, 255));
                                markerOptions.icon(vectorToBitmap(R.drawable.ic_eventmark));
                                break;

                            case "gym":
                                circleOptions.strokeColor(Color.argb(255, 0, 0, 255));
                                circleOptions.fillColor(Color.argb(64, 0, 0, 255));
                                markerOptions.icon(vectorToBitmap(R.drawable.ic_gym));
                                break;

                            case "store":
                                circleOptions.strokeColor(Color.argb(255, 0, 0, 255));
                                circleOptions.fillColor(Color.argb(64, 0, 0, 255));
                                markerOptions.icon(vectorToBitmap(R.drawable.ic_online_shopping));
                                break;

                            default:
                                circleOptions.strokeColor(Color.argb(255, 255, 100, 100));
                                circleOptions.fillColor(Color.argb(64, 255, 100, 100));
                        }

                        circleOptions.strokeWidth(4);
                        if (updateTest.equals("All")) {
                            mMap.addCircle(circleOptions);
                            mMap.addMarker(markerOptions);
                        } else {
                            if (loc.child("type").getValue().toString().equals(updateTest)) {
                                mMap.addCircle(circleOptions);
                                mMap.addMarker(markerOptions);
                            }
                        }


                    } catch (NullPointerException e) {
                        Log.d("@@@@", "onDataChange: " + e.getMessage());
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "dataSnapshot Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void moveCamera() {


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (isGPS()) {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.5f));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "no location", Toast.LENGTH_SHORT).show();

                }
            });

            if (!isInternet()) {
                Toast.makeText(getContext(), "Turn on Internet to show map information", Toast.LENGTH_SHORT).show();
            }

        } else if (!isGPS() && isInternet()) {

            Toast.makeText(getContext(), "Please turn on GPS to get latest places updates", Toast.LENGTH_SHORT).show();

            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addressList = new ArrayList<>();

            try {
                addressList = geocoder.getFromLocationName(usersCity, 1);

            } catch (Exception ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (addressList.size() > 0) {
                Address address = addressList.get(0);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 14.5f));

            }

        } else if (!isInternet() && !isGPS()) {
            Toast.makeText(getContext(), "Please turn on Internet and location", Toast.LENGTH_SHORT).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.23648, 31.26584), 14.5f));

        }

    }

    public Boolean isGPS() {

        LocationManager lm = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public Boolean isInternet() {

        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUST_CODE) {

            if (grantResults.length > 0
                    && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                initMap();
                HomeFragment homeFragment = new HomeFragment();

            } else {
                Intent webIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(webIntent);
            }
        } else if (requestCode == 999) {
            if (grantResults.length > 0
                    && (grantResults[0] + grantResults[1] + grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                initMap();

            } else {
                Intent webIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(webIntent);
            }
        }

    }

    public void getLocationPermissions() {
        //Toast.makeText(getContext(), "permission", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) +
                    ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) +
                    ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        },
                        999
                );
                return;

            }
            initMap();

        } else {


            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) +
                    ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                        },
                        REQUST_CODE
                );
                return;
            }
            initMap();
        }
    }


    public void initMap() {
        //Toast.makeText(getContext(), "initMap", Toast.LENGTH_SHORT).show();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            // Toast.makeText(getContext(), "!null", Toast.LENGTH_SHORT).show();

            mapFragment.getMapAsync(callback);
        }

    }


    private void setSearchAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.city_drop_menu, places);
        placesSearch.setAdapter(adapter);
    }

    private Boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo :
                activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (GeofireService.class.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Boolean isNotificationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo :
                activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private BitmapDescriptor vectorToBitmap(@DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        assert vectorDrawable != null;
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
