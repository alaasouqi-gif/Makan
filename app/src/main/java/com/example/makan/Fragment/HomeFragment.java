package com.example.makan.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.activity.list.list_home;
import com.example.makan.adapter.adapter_card;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        super();
    }

    FusedLocationProviderClient fusedLocationProviderClient;

    ArrayList<String> placesAsString = new ArrayList<>();
    ArrayList<Double> placesAsDouble = new ArrayList<>();

    double rate;
    Long visits;
    Long books;
    String userCity;
    Long interested;
    Long tickets;
    double p;

    Location mLocation;
    int c = 0;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView list_near;
    RecyclerView list_interests;
    RecyclerView list_popular;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    public static ArrayList<event> popularEvents = new ArrayList<>();
    public static ArrayList<event> forYouEvents = new ArrayList<>();
    public static ArrayList<event> nearEvents = new ArrayList<>();

    ArrayList<String> nearPlaces = new ArrayList<>();

    adapter_card adapter1;
    adapter_card adapter3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        nearEvents.clear();
        nearPlaces.clear();

        popularEvents.clear();
        forYouEvents.clear();

        placesAsString.clear();
        placesAsDouble.clear();

        list_near = root.findViewById(R.id.list_following);
        list_interests = root.findViewById(R.id.list_interests);
        list_popular = root.findViewById(R.id.list_popular);

        linearLayout = root.findViewById(R.id.linear);
        progressBar = root.findViewById(R.id.progressBar);

        list_popular.setHasFixedSize(true);
        list_popular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        list_near.setHasFixedSize(true);
        list_near.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        list_interests.setHasFixedSize(true);
        list_interests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        p = 0;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                popularEvents.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    rate = Double.parseDouble(String.valueOf(dataSnapshot.child("rate").getValue()));
                    //rate=2.2;
                    visits = (Long) dataSnapshot.child("visits").getValue();
                    books = (Long) dataSnapshot.child("books").getValue();
                    interested = (Long) dataSnapshot.child("interested").getValue();
                    tickets = (Long) dataSnapshot.child("tickets").getValue();

                    switch (dataSnapshot.child("type").getValue().toString()) {
                        case "restaurant":
                        case "pitch":
                        case "gym":
                        case "store":
                            if (books > visits) {
                                p = 1;
                            } else if (visits != 0) {
                                p = (rate / 5) * 0.25 + (books.doubleValue() / visits.doubleValue()) * 0.75;
                            } else {
                                p = 0;
                            }
                            placesAsDouble.add(p);
                            placesAsString.add(dataSnapshot.getKey() + "-//" + p);
                            break;
                        case "event":
                            if (tickets > visits) {
                                p = 1;
                            } else if (visits != 0) {
                                p = (interested.doubleValue() / visits.doubleValue()) * 0.3 + (tickets.doubleValue() / visits.doubleValue()) * 0.7;
                            } else {
                                p = 0;
                            }
                            placesAsDouble.add(p);
                            placesAsString.add(dataSnapshot.getKey() + "-//" + p);
                            break;
                    }
                }

                Collections.sort(placesAsDouble, Collections.<Double>reverseOrder());
                String temp;

                for (int j = 0; j < placesAsString.size(); j++) {
                    if (!placesAsString.get(j).split("-//")[1].equals(placesAsDouble.get(j).toString())) {
                        temp = placesAsString.get(j);
                        for (String place : placesAsString) {
                            if (place.split("-//")[1].equals(placesAsDouble.get(j).toString())) {
                                Collections.swap(placesAsString, placesAsString.indexOf(place), placesAsString.indexOf(temp));
                                break;
                            }
                        }
                    }
                }

                for (String s : placesAsString) {
                    //Log.d("PLACE", "onDataChange: "+s+"................................................");

                }

                final ArrayList<DataSnapshot> popular_key = new ArrayList<>();
                for (int i = 0; i < placesAsString.size(); i++) {
                    popular_key.add(snapshot.child(placesAsString.get(i).split("-//")[0]));
                    popularEvents.add(new event(popular_key.get(i).child("name").getValue().toString()
                            , popular_key.get(i).child("city").getValue().toString()
                            , popular_key.get(i).child("des").getValue().toString()
                            , popular_key.get(i).getKey()
                            , popular_key.get(i).child("type").getValue().toString()
                            , Uri.parse(popular_key.get(i).child("cover photo").getValue().toString())));

                }

                adapter1 = new adapter_card(popularEvents, getActivity());
                list_popular.setAdapter(adapter1);


                FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("interests").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot user_snapshot) {
                        forYouEvents.clear();

                        ArrayList<String> category = new ArrayList<>();
                        ArrayList<String> categories = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : user_snapshot.getChildren()) {

                            category.add(dataSnapshot.getValue().toString());
                        }

                        for (int i = 0; i < popular_key.size(); i++) {
                            categories.clear();

                            for (DataSnapshot dataSnapshot : popular_key.get(i).child("categories").getChildren()) {
                                categories.add(dataSnapshot.getValue().toString());
                                Log.d("TAG", "onDataChange: ooo" + categories);

                            }
                            for (int j = 0; j < categories.size(); j++) {
                                if (category.contains(categories.get(j))) {
                                    DataSnapshot a = popular_key.get(i);
                                    forYouEvents.add(new event(
                                            a.child("name").getValue().toString()
                                            , a.child("city").getValue().toString()
                                            , a.child("des").getValue().toString()
                                            , a.getKey()
                                            , a.child("type").getValue().toString()
                                            , Uri.parse(a.child("cover photo").getValue().toString())));
                                    break;
                                }
                            }


                        }
                        adapter3 = new adapter_card(forYouEvents, getActivity());
                        list_interests.setAdapter(adapter3);
                        if (c == 1) {
                            progressBar.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);

                        } else c = 1;


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                nearYou();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });






/*        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("interests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot_user) {

                FirebaseDatabase.getInstance().getReference("services").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot_services) {

                        for(DataSnapshot dataSnapshot:snapshot_user.getChildren()){

                            category.add(dataSnapshot.getValue().toString());
                        }
                        for(DataSnapshot dataSnapshot:snapshot_services.getChildren()){

                            ser.add(dataSnapshot.getValue().toString());
                        }
                        for (int i = 0; i <ser.size(); i++) {
                            snapshot_services.child(ser.get(i))


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        TextView near_text = root.findViewById(R.id.near);
        TextView Interests_text = root.findViewById(R.id.Interests);
        TextView popular_text = root.findViewById(R.id.popular);

        near_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), list_home.class);
                intent.putExtra("title", "Near You");
                intent.putExtra("key", 0);
                startActivity(intent);
            }
        });

        Interests_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), list_home.class);
                intent.putExtra("title", "For You");
                intent.putExtra("key", 1);

                startActivity(intent);
            }
        });

        popular_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), list_home.class);
                intent.putExtra("title", "Popular");
                intent.putExtra("key", 2);

                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nearEvents.clear();
        nearPlaces.clear();

        getLocationPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 556) {

            if (grantResults.length > 0
                    && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

                nearYou();

            } else {
                Intent webIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(webIntent);
            }
        } else if (requestCode == 998) {
            if (grantResults.length > 0
                    && (grantResults[0] + grantResults[1] + grantResults[2] == PackageManager.PERMISSION_GRANTED)) {

                nearYou();

            } else {
                Intent webIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                startActivity(webIntent);
            }
        }
    }

    private void nearYou() {

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull final Task<Location> task) {
                mLocation = task.getResult();

                FirebaseDatabase.getInstance().getReference("services").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull final DataSnapshot snapshot) {
                        nearEvents.clear();
                        nearPlaces.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            double lat = (double) dataSnapshot.child("lat").getValue();
                            double lng = (double) dataSnapshot.child("lng").getValue();
                            if (distance(lat, lng, mLocation.getLatitude(), mLocation.getLongitude()) <= 3.5) {
                                Log.d("distance", "onDataChange: between is: " + distance(lat, lng, mLocation.getLatitude(), mLocation.getLongitude()));
                                nearPlaces.add(dataSnapshot.getKey());
                            }
                        }

                        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot_1) {

                                userCity = snapshot_1.child("City").getValue().toString();
                                if (nearPlaces.size() == 0) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.d("near you", "onDataChange: xxx............................................");

                                        if (dataSnapshot.child("city").getValue().toString().equals(userCity)) {
                                            nearPlaces.add(dataSnapshot.getKey());

                                        }
                                    }
                                }

                                for (int i = 0; i < nearPlaces.size(); i++) {
                                    DataSnapshot a = snapshot.child(nearPlaces.get(i));
                                    try {
                                        nearEvents.add(new event(
                                                a.child("name").getValue().toString()
                                                , a.child("city").getValue().toString()
                                                , a.child("des").getValue().toString()
                                                , a.getKey()
                                                , a.child("type").getValue().toString()
                                                , Uri.parse(a.child("cover photo").getValue().toString())));
                                    } catch (Exception e) {

                                    }

                                }
                                adapter_card adapter2 = new adapter_card(nearEvents, getActivity());
                                list_near.setAdapter(adapter2);
                                if (c == 1) {
                                    progressBar.setVisibility(View.GONE);
                                    linearLayout.setVisibility(View.VISIBLE);

                                } else c = 1;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }

    public void getLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
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
                        998
                );
                return;

            }

            nearYou();

        } else {


            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
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
                        556
                );
                return;
            }

            nearYou();
        }
    }


    public double distance(double lat1, double lng1, double lat2, double lng2) {
        double lngDif = lng1 - lng2;
        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(lngDif));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;
        distance = distance * 1.609344;
        return distance;
    }

    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(double lat1) {
        return (lat1 * Math.PI / 180.0);
    }


    @Override
    public void onResume() {
        super.onResume();

    }


}

