package com.example.makan.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Statistics {

    double rate;
    Long visits;
    Long books;

    String placeId;

    Long interested;
    Long tickets;

    DatabaseReference reference;


    ArrayList<Double> restRates = new ArrayList<>();
    ArrayList<Double> evRates = new ArrayList();

    ArrayList<String> ratedRes = new ArrayList();
    ArrayList<String> ratedEv = new ArrayList();

    public Statistics(DatabaseReference reference) {
        this.reference = reference;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rate = (double) snapshot.child("rate").getValue();
                visits = (Long) snapshot.child("visits").getValue();
                books = (Long) snapshot.child("books").getValue();
                interested = (Long) snapshot.child("interested").getValue();
                tickets = (Long) snapshot.child("tickets").getValue();
                placeId = snapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    Boolean setVisit;

    public void setVisit(final DatabaseReference reference) {
        setVisit = true;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                visits = (Long) snapshot.child("visits").getValue();
                try {
                    for (DataSnapshot dataSnapshot : snapshot.child("visitors").getChildren()) {
                        if (dataSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            setVisit = false;
                            break;
                        }
                    }
                    if (setVisit) {
                        visits++;
                        reference.child("visits").setValue(visits);
                        reference.child("visitors").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                } catch (NullPointerException e) {
                    visits++;
                    reference.child("visits").setValue(visits);
                    reference.child("visitors").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    Boolean setRate;

    public void setRate(final DatabaseReference reference) {
        setRate = true;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rate = (double) snapshot.child("rate").getValue();
                try {
                    for (DataSnapshot dataSnapshot : snapshot.child("raters").getChildren()) {
                        if (dataSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            setRate = false;
                            break;
                        }
                    }
                    if (setRate) {
                        //Some Code To Calculate Rate
                        reference.child("visitors").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                } catch (NullPointerException e) {
                    //Some Code To Calculate Rate
                    reference.child("visitors").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    Boolean setInterested;

    public void setInterested(final DatabaseReference reference) {
        setInterested = true;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                interested = (Long) snapshot.child("interested").getValue();
                try {
                    for (DataSnapshot dataSnapshot : snapshot.child("intr").getChildren()) {
                        if (dataSnapshot.getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            setInterested = false;
                            break;
                        }
                    }
                    if (setInterested) {
                        interested++;
                        reference.child("interested").setValue(interested);
                        reference.child("intr").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                } catch (NullPointerException e) {
                    interested++;
                    reference.child("interested").setValue(interested);
                    reference.child("intr").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setBooks(DatabaseReference reference) {
        books++;
        reference.child("books").setValue(books);
    }

    public void setTickets(DatabaseReference reference) {
        tickets++;
        reference.child("tickets").setValue(tickets);
    }

    double p;

    public ArrayList<String> getPopularity(String type) {

        p = 0;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    switch (dataSnapshot.child("type").getValue().toString()) {
                        case "restaurant":
                        case "pitch":
                            if (books > visits) {
                                p = 1;
                            } else {
                                p = (rate / 5) * 0.25 + (books.doubleValue() / visits.doubleValue()) * 0.75;
                                restRates.add(p);
                                ratedRes.add(dataSnapshot.getKey() + "-//" + p);
                            }
                            break;
                        case "event":
                            if (tickets > interested) {
                                p = 1;
                            } else {
                                p = (interested.doubleValue() / visits.doubleValue()) * 0.3 + (tickets.doubleValue() / interested.doubleValue()) * 0.7;
                                evRates.add(p);
                                ratedEv.add(dataSnapshot.getKey() + "-//" + p);

                            }
                            break;
                    }
                }
                Collections.sort(restRates, Collections.<Double>reverseOrder());
                Collections.sort(evRates, Collections.<Double>reverseOrder());

                String temp;

                for (int j = 0; j < ratedEv.size(); j++) {
                    Log.d("FOR LOOP", "onDataChange: " + j + "..size" + ratedEv.size());
                    if (!ratedEv.get(j).split("-//")[1].equals(evRates.get(j).toString())) {
                        temp = ratedEv.get(j);
                        for (String place : ratedEv) {
                            if (place.split("-//")[1].equals(evRates.get(j).toString())) {
                                ratedEv.set(ratedEv.indexOf(temp), place);
                                ratedEv.set(ratedEv.indexOf(place), temp);
                                break;
                            }
                        }
                    }
                }

                for (int j = 0; j < ratedRes.size(); j++) {
                    Log.d("FOR LOOP", "onDataChange: " + j + "..size" + ratedRes.size());
                    if (!ratedRes.get(j).split("-//")[1].equals(restRates.get(j).toString())) {
                        temp = ratedRes.get(j);
                        for (String place : ratedRes) {
                            if (place.split("-//")[1].equals(evRates.get(j).toString())) {
                                ratedRes.set(ratedRes.indexOf(temp), place);
                                ratedRes.set(ratedRes.indexOf(place), temp);
                                break;
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (type.equals("restaurant") || type.equals("pitch")) {
            return ratedRes;

        }

        if (type.equals("event")) {
            return ratedEv;
        }
        return null;
    }


    public String s() {
        String temp;

        for (int j = 0; j < ratedEv.size(); j++) {
            Log.d("FOR LOOP", "onDataChange: " + j + "..size" + ratedEv.size());
            if (!ratedEv.get(j).split("-//")[1].equals(evRates.get(j).toString())) {
                temp = ratedEv.get(j);
                for (String place : ratedEv) {
                    if (place.split("-//")[1].equals(evRates.get(j).toString())) {
                        ratedEv.set(ratedEv.indexOf(temp), place);
                        ratedEv.set(ratedEv.indexOf(place), temp);
                        break;
                    }
                }
            }
        }

        StringBuilder s = new StringBuilder();
        for (String s1 : ratedEv) {
            s.append(s1.split("-//")[0]).append("**");
        }

        return s.toString();

    }

    public double getReliability() {
        return books.doubleValue() / visits.doubleValue();
    }

    public double getRate() {
        return rate;
    }

    public Long getVisits() {
        return visits;
    }

    public Long getInterested() {
        return interested;
    }
}
