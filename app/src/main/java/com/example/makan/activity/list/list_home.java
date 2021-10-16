package com.example.makan.activity.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.Fragment.HomeFragment;
import com.example.makan.R;
import com.example.makan.adapter.adapter_res;

import java.util.ArrayList;

public class list_home extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    private ArrayList<event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_home);

        Intent intent = getIntent();
        int key = intent.getIntExtra("key", 0);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("title"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch (key) {
            case 0:
                events.addAll(HomeFragment.nearEvents);
                break;
            case 1:
                events.addAll(HomeFragment.forYouEvents);
                break;
            case 2:
                events.addAll(HomeFragment.popularEvents);
                break;
            default:
                break;

        }

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter_res adapter = new adapter_res(events, getApplicationContext());
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

       /* myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (String key:id) {
                    events.add(new event(
                            dataSnapshot.child(key).child("name").getValue().toString()
                            ,dataSnapshot.child(key).child("city").getValue().toString()
                            ,dataSnapshot.child(key).child("des").getValue().toString()
                            ,key
                            ,dataSnapshot.child(key).child("type").getValue().toString()
                            , Uri.parse(dataSnapshot.child(key).child("cover photo").getValue().toString())
                    ) );

                }
                adapter_res adapter = new adapter_res(events, getApplicationContext());
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }
}