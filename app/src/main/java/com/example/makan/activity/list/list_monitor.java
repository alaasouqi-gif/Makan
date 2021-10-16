package com.example.makan.activity.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.monitor;
import com.example.makan.R;
import com.example.makan.adapter.adapter_monitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_monitor extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    private ArrayList<monitor> monitors = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users");
    String key;
    String type;
    DatabaseReference reservation_Ref = database.getReference("services");
    RelativeLayout message;
    adapter_monitor adapter;


    @Override
    protected void onPostResume() {

        super.onPostResume();
        monitors.clear();

        reservation_Ref.child(key).child("subscriptions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);

                } else {

                    user_ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                            monitors.clear();

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                String key_pay = d.getKey();
                                String key_user = String.valueOf(d.child("user").getValue());

                                DataSnapshot Snapshot1 = dataSnap.child(key_user);
                                //Log.d("TAG", "onDataChange: nnnnn"+Snapshot1.getValue());
                                switch (type) {
                                    case "event":
                                        monitors.add(new monitor(
                                                "Name : " + Snapshot1.child("First Name").getValue() + " " + dataSnap.child(key_user).child("Last Name").getValue()
                                                , "Amount : " + d.child("amount").getValue()
                                                , "Ticket number : " + d.child("tickets number").getValue()
                                                , "Payment time : " + d.getKey()
                                                , "Email : " + Snapshot1.child("Email").getValue()
                                                , "Phone : " + Snapshot1.child("Phone Number").getValue()
                                                , "Id : " + d.child("id").getValue()));
                                        break;

                                    case "restaurant":
                                        monitors.add(new monitor(
                                                "Name : " + Snapshot1.child("First Name").getValue() + " " + dataSnap.child(key_user).child("Last Name").getValue()
                                                , "Amount : " + d.child("amount").getValue()
                                                , "Date : " + d.child("date").getValue() + " " + d.child("time").getValue()
                                                , "Payment time : " + d.getKey()
                                                , "Email : " + Snapshot1.child("Email").getValue()
                                                , "Phone : " + Snapshot1.child("Phone Number").getValue()
                                                , "Id : " + d.child("id").getValue()));
                                        break;


                                    case "gym":
                                        monitors.add(new monitor(
                                                "Name : " + Snapshot1.child("First Name").getValue() + " " + dataSnap.child(key_user).child("Last Name").getValue()
                                                , "Amount : " + d.child("amount").getValue()
                                                , "Start Subscription : " + d.child("date").getValue() + "\n\nPeriod Subscription : " + d.child("period").getValue() + " Months"
                                                , "Payment time : " + d.getKey()
                                                , "Email : " + Snapshot1.child("Email").getValue()
                                                , "Phone : " + Snapshot1.child("Phone Number").getValue()
                                                , "Id : " + d.child("id").getValue()));
                                        break;
                                    case "pitch":
                                        monitors.add(new monitor(

                                                "Name : " + Snapshot1.child("First Name").getValue() + " " + dataSnap.child(key_user).child("Last Name").getValue()
                                                , "Amount : " + d.child("amount").getValue()
                                                , "Start Date : " + d.child("date").getValue() + " - " + d.child("time").getValue() + "\n\nPeriod : " + d.child("period").getValue() + " Hours"
                                                , "Payment time : " + d.getKey()
                                                , "Email : " + Snapshot1.child("Email").getValue()
                                                , "Phone : " + Snapshot1.child("Phone Number").getValue()
                                                , "Id : " + d.child("id").getValue()));
                                        break;

                                }


                            }

                            adapter = new adapter_monitor(monitors, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_monitor);
        Intent intent = getIntent();

        key = intent.getStringExtra("sid");
        type = intent.getStringExtra("type");

        toolbar = findViewById(R.id.toolbar);
        message = findViewById(R.id.message);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.reservation_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}