package com.example.makan.activity.list;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.reservation;
import com.example.makan.R;
import com.example.makan.adapter.adapter_reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_Reservations extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    private ArrayList<reservation> reservations = new ArrayList<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid()).child("reservations");
    DatabaseReference reservation_Ref = database.getReference("services");
    RelativeLayout message;
    adapter_reservation adapter;

    @Override
    protected void onPostResume() {

        super.onPostResume();
        reservations.clear();
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);

                } else {

                    reservation_Ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                            reservations.clear();

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                String key_pay = d.getKey();
                                String key_service = String.valueOf(d.child("service").getValue());

                                DataSnapshot Snapshot1 = dataSnap.child(key_service).child("subscriptions").child(key_pay);
                                //Log.d("TAG", "onDataChange: nnnnn"+Snapshot1.getValue());
                                switch (dataSnap.child(key_service).child("type").getValue().toString()) {
                                    case "restaurant":
                                        reservations.add(new reservation(
                                                dataSnap.child(key_service).child("name").getValue().toString()
                                                , dataSnap.child(key_service).child("cover photo").getValue().toString()
                                                , key_service
                                                , dataSnap.child(key_service).child("type").getValue().toString()
                                                , "Reservation Date : " + Snapshot1.child("date").getValue() + " - " + Snapshot1.child("time").getValue()
                                                , Snapshot1.child("id").getValue().toString()
                                                , key_pay
                                                , Snapshot1.child("amount").getValue().toString()
                                                , dataSnap.child(key_service).child("open").getValue().toString()
                                                , dataSnap.child(key_service).child("close").getValue().toString()));
                                        break;

                                    case "event":
                                        reservations.add(new reservation(
                                                dataSnap.child(key_service).child("name").getValue().toString()
                                                , dataSnap.child(key_service).child("cover photo").getValue().toString()
                                                , key_service
                                                , dataSnap.child(key_service).child("type").getValue().toString()
                                                , "Start Date : " + dataSnap.child(key_service).child("open").getValue().toString() + "\nEnd Date : " + dataSnap.child(key_service).child("close").getValue().toString()
                                                , Snapshot1.child("id").getValue().toString()
                                                , key_pay
                                                , Snapshot1.child("amount").getValue().toString()
                                                , Snapshot1.child("tickets number").getValue().toString()
                                        ));
                                        break;
                                    case "gym":
                                        reservations.add(new reservation(
                                                dataSnap.child(key_service).child("name").getValue().toString()
                                                , dataSnap.child(key_service).child("cover photo").getValue().toString()
                                                , key_service
                                                , dataSnap.child(key_service).child("type").getValue().toString()
                                                , "Start Subscription : " + Snapshot1.child("date").getValue().toString() + "\nPeriod Subscription : " + Snapshot1.child("period").getValue().toString() + " Months"
                                                , Snapshot1.child("id").getValue().toString()
                                                , key_pay
                                                , Snapshot1.child("amount").getValue().toString()
                                                , dataSnap.child(key_service).child("open").getValue().toString()
                                                , dataSnap.child(key_service).child("close").getValue().toString()));
                                        break;
                                    case "pitch":
                                        reservations.add(new reservation(
                                                dataSnap.child(key_service).child("name").getValue().toString()
                                                , dataSnap.child(key_service).child("cover photo").getValue().toString()
                                                , key_service
                                                , dataSnap.child(key_service).child("type").getValue().toString()
                                                , "Start Date : " + Snapshot1.child("date").getValue() + " - " + Snapshot1.child("time").getValue() + "\nPeriod : " + Snapshot1.child("period").getValue().toString() + " Hours"
                                                , Snapshot1.child("id").getValue().toString()
                                                , key_pay
                                                , Snapshot1.child("amount").getValue().toString()
                                                , dataSnap.child(key_service).child("open").getValue().toString()
                                                , dataSnap.child(key_service).child("close").getValue().toString()));
                                        break;

                                }


                            }

                            adapter = new adapter_reservation(reservations, getApplicationContext());
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
        setContentView(R.layout.activity_list_reservations);


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