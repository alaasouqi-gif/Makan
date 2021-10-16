package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.activity.add_services.add_menu;
import com.example.makan.adapter.adapter_edit_category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class edit_menu extends AppCompatActivity {
    Toolbar toolbar;
    private ArrayList<event> events = new ArrayList<>();
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference servicesRef = database.getReference("services");
    RelativeLayout message;
    adapter_edit_category adapter;
    String sid;
    ArrayList<String> cat = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu);


        toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();

        sid = intent.getStringExtra("sid");


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        servicesRef.child(sid).child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                cat.clear();

                if (dataSnapshot.getChildrenCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                } else {
                    events.clear();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {

                        cat.add(d.getKey());
                    }


                }

                adapter = new adapter_edit_category(cat, getApplicationContext(), sid);
                recyclerView.setAdapter(adapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        message = findViewById(R.id.nothing);

        recyclerView = findViewById(R.id.list_services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.services, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        PopupMenu popup = new PopupMenu(edit_menu.this, findViewById(R.id.add));
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add) {
                    Intent intent = new Intent(edit_menu.this, add_menu.class);
                    startActivity(intent);
                } else {
                    return true;
                }
                return false;
            }
        });

        popup.show();
        return super.onOptionsItemSelected(item);
    }

}
