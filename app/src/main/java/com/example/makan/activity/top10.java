package com.example.makan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.adapter.adapter_following;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class top10 extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("services");
    double rate;
    Long visits;
    Long books;
    Long tickets;
    Long interested;
    double p;
    String TopCategory;
    ArrayList<String> placesAsString = new ArrayList<>();
    ArrayList<Double> placesAsDouble = new ArrayList<>();
    ArrayList<Long> placesAsLong = new ArrayList<>();
    ProgressBar progressBar;
    private ArrayList<event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);
        Intent intent = getIntent();
        TopCategory = intent.getStringExtra("TopCategory");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(TopCategory);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        switch (TopCategory) {
            case "Top Rated":
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            rate = Double.parseDouble(dataSnapshot1.child("rate").getValue().toString());
                            placesAsDouble.add(rate);
                            placesAsString.add(dataSnapshot1.getKey() + "-//" + rate);
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
                        ArrayList<String> value = new ArrayList<>();
                        ArrayList<DataSnapshot> dataSnapshotKey = new ArrayList<>();
                        for (int i = 0; i < placesAsString.size(); i++) {
                            value.add(placesAsString.get(i).split("-//")[1]);
                            dataSnapshotKey.add(dataSnapshot.child(placesAsString.get(i).split("-//")[0]));
                        }
                        for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                            events.add(new event(
                                    dataSnapshotKey.get(i).child("name").getValue().toString()
                                    , value.get(i).substring(0, Math.min(3, value.get(i).length())) + " / 5 "
                                    , dataSnapshotKey.get(i).getKey()
                                    , dataSnapshotKey.get(i).child("type").getValue().toString()
                                    , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                        }
                        adapter_following adapter = new adapter_following(events, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            case "Most Visited":
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            visits = (Long) dataSnapshot1.child("visits").getValue();
                            placesAsLong.add(visits);
                            placesAsString.add(dataSnapshot1.getKey() + "-//" + visits);
                        }
                        Collections.sort(placesAsLong, Collections.<Long>reverseOrder());
                        String temp;
                        for (int j = 0; j < placesAsString.size(); j++) {
                            if (!placesAsString.get(j).split("-//")[1].equals(placesAsLong.get(j).toString())) {
                                temp = placesAsString.get(j);
                                for (String place : placesAsString) {
                                    if (place.split("-//")[1].equals(placesAsLong.get(j).toString())) {
                                        Collections.swap(placesAsString, placesAsString.indexOf(place), placesAsString.indexOf(temp));
                                        break;
                                    }
                                }
                            }
                        }
                        ArrayList<String> value = new ArrayList<>();
                        ArrayList<DataSnapshot> dataSnapshotKey = new ArrayList<>();
                        for (int i = 0; i < placesAsString.size(); i++) {
                            value.add(placesAsString.get(i).split("-//")[1]);
                            dataSnapshotKey.add(dataSnapshot.child(placesAsString.get(i).split("-//")[0]));

                        }
                        for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                            events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                    , value.get(i) + " Visited "
                                    , dataSnapshotKey.get(i).getKey()
                                    , dataSnapshotKey.get(i).child("type").getValue().toString()
                                    , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                        }
                        adapter_following adapter = new adapter_following(events, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                break;
            default:
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        events.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            rate = Double.parseDouble(dataSnapshot.child("rate").getValue().toString());
                            //rate=2.2;
                            visits = (Long) dataSnapshot.child("visits").getValue();
                            books = (Long) dataSnapshot.child("books").getValue();
                            tickets = (Long) dataSnapshot.child("tickets").getValue();
                            interested = (Long) dataSnapshot.child("interested").getValue();
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
                        ArrayList<String> value = new ArrayList<>();
                        ArrayList<DataSnapshot> dataSnapshotKey = new ArrayList<>();
                        for (int i = 0; i < placesAsString.size(); i++) {
                            value.add(placesAsString.get(i).split("-//")[1]);
                            dataSnapshotKey.add(snapshot.child(placesAsString.get(i).split("-//")[0]));
                        }
                        switch (TopCategory) {
                            case "Top 10":
                                for (int i = 0; i < Math.min(placesAsString.size(), 10); i++) {
                                    String s = "";
                                    if (value.get(i).substring(2, Math.min(4, value.get(i).length())).length() == 1) {
                                        s = value.get(i).substring(2, Math.min(4, value.get(i).length())) + "0";
                                    } else {
                                        s = value.get(i).substring(2, Math.min(4, value.get(i).length()));
                                    }
                                    events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                            , s + "%"
                                            , dataSnapshotKey.get(i).getKey()
                                            , dataSnapshotKey.get(i).child("type").getValue().toString()
                                            , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                                }
                                break;
                            case "Top Restaurant":
                                for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                                    if (dataSnapshotKey.get(i).child("type").getValue().equals("restaurant")) {
                                        String s = "";
                                        if (value.get(i).substring(2, Math.min(4, value.get(i).length())).length() == 1) {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length())) + "0";
                                        } else {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length()));
                                        }
                                        events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                                , s + "%"
                                                , dataSnapshotKey.get(i).getKey()
                                                , dataSnapshotKey.get(i).child("type").getValue().toString()
                                                , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                                    }
                                }
                                break;
                            case "Top Events":
                                for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                                    if (dataSnapshotKey.get(i).child("type").getValue().equals("event")) {
                                        String s = "";
                                        if (value.get(i).substring(2, Math.min(4, value.get(i).length())).length() == 1) {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length())) + "0";
                                        } else {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length()));
                                        }
                                        events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                                , s + "%"
                                                , dataSnapshotKey.get(i).getKey()
                                                , dataSnapshotKey.get(i).child("type").getValue().toString()
                                                , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                                    }
                                }
                                break;
                            case "Top Stores":
                                for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                                    if (dataSnapshotKey.get(i).child("type").getValue().equals("store")) {
                                        String s = "";
                                        if (value.get(i).substring(2, Math.min(4, value.get(i).length())).length() == 1) {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length())) + "0";
                                        } else {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length()));
                                        }
                                        events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                                , s + "%"
                                                , dataSnapshotKey.get(i).getKey()
                                                , dataSnapshotKey.get(i).child("type").getValue().toString()
                                                , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                                    }
                                }
                                break;
                            case "Top Gyms":
                                for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                                    if (dataSnapshotKey.get(i).child("type").getValue().equals("gym")) {
                                        String s = "";
                                        if (value.get(i).substring(2, Math.min(4, value.get(i).length())).length() == 1) {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length())) + "0";
                                        } else {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length()));
                                        }
                                        events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                                , s + "%"
                                                , dataSnapshotKey.get(i).getKey()
                                                , dataSnapshotKey.get(i).child("type").getValue().toString()
                                                , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                                    }
                                }
                                break;
                            case "Top Pitch":
                                for (int i = 0; i < placesAsString.size() & events.size() < 10; i++) {
                                    if (dataSnapshotKey.get(i).child("type").getValue().equals("pitch")) {
                                        String s = "";
                                        if (value.get(i).substring(2, Math.min(4, value.get(i).length())).length() == 1) {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length())) + "0";
                                        } else {
                                            s = value.get(i).substring(2, Math.min(4, value.get(i).length()));
                                        }
                                        events.add(new event(dataSnapshotKey.get(i).child("name").getValue().toString()
                                                , s + "%"
                                                , dataSnapshotKey.get(i).getKey()
                                                , dataSnapshotKey.get(i).child("type").getValue().toString()
                                                , Uri.parse(dataSnapshotKey.get(i).child("cover photo").getValue().toString())));
                                    }
                                }
                                break;
                        }


                        adapter_following adapter = new adapter_following(events, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        }
    }
}
