package com.example.makan.activity.list;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.adapter.adapter_res;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_check_later extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    private ArrayList<event> events = new ArrayList<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid()).child("check later");
    DatabaseReference checkLaterRef = database.getReference("services");
    RelativeLayout message;
    adapter_res adapter;

    @Override
    protected void onPostResume() {

        super.onPostResume();
        events.clear();
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                } else {

                    checkLaterRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                            events.clear();

                            for (DataSnapshot d : dataSnapshot.getChildren()) {

                                events.add(new event(dataSnap.child(d.getValue().toString()).child("name").getValue().toString()
                                        , dataSnap.child(d.getValue().toString()).child("city").getValue().toString()
                                        , dataSnap.child(d.getValue().toString()).child("des").getValue().toString()
                                        , dataSnap.child(d.getValue().toString()).getKey()
                                        , dataSnap.child(d.getValue().toString()).child("type").getValue().toString()
                                        , Uri.parse(dataSnap.child(d.getValue().toString()).child("cover photo").getValue().toString())));
                            }
                            adapter = new adapter_res(events, getApplicationContext());
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
        setContentView(R.layout.activity_list_check_later);

        events.clear();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        message = findViewById(R.id.message);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
