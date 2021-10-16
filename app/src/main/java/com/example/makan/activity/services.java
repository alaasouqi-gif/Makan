package com.example.makan.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.event;
import com.example.makan.R;
import com.example.makan.activity.add_services.add_Shopping;
import com.example.makan.activity.add_services.add_event;
import com.example.makan.activity.add_services.add_gym;
import com.example.makan.activity.add_services.add_pitch;
import com.example.makan.activity.add_services.add_restaurant;
import com.example.makan.adapter.adapter_services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class services extends AppCompatActivity {
    Toolbar toolbar;
    private ArrayList<event> events = new ArrayList<>();
    RecyclerView recyclerView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid()).child("services");
    DatabaseReference servicesRef = database.getReference("services");
    RelativeLayout message;
    adapter_services adapter;

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

                    servicesRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnap) {
                            events.clear();

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                try {


                                    events.add(new event(
                                            dataSnap.child(d.getValue().toString()).child("name").getValue().toString()
                                            , dataSnap.child(d.getValue().toString()).child("city").getValue().toString()
                                            , dataSnap.child(d.getValue().toString()).child("des").getValue().toString()
                                            , dataSnap.child(d.getValue().toString()).getKey()
                                            , dataSnap.child(d.getValue().toString()).child("type").getValue().toString()
                                            , Uri.parse(dataSnap.child(d.getValue().toString()).child("cover photo").getValue().toString())));
                                } catch (Exception e) {

                                }
                            }
                            adapter = new adapter_services(events, getApplicationContext());
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
        setContentView(R.layout.activity_services);

        toolbar = findViewById(R.id.toolbar);

        requestPermission();


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(services.this, MainActivity.class);
                startActivity(intent);
            }
        });

        message = findViewById(R.id.nothing);

        recyclerView = findViewById(R.id.list_services);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(services.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
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
        PopupMenu popup = new PopupMenu(services.this, findViewById(R.id.add));
        popup.getMenuInflater().inflate(R.menu.pop_create, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.event:
                        Intent intent = new Intent(services.this, add_event.class);
                        startActivity(intent);
                        break;
                    case R.id.restaurant:
                        intent = new Intent(services.this, add_restaurant.class);
                        startActivity(intent);
                        break;
                    case R.id.gym:
                        intent = new Intent(services.this, add_gym.class);
                        startActivity(intent);
                        break;

                    case R.id.Mall:
                        intent = new Intent(services.this, add_Shopping.class);
                        startActivity(intent);
                        break;
                    case R.id.football:
                        intent = new Intent(services.this, add_pitch.class);
                        startActivity(intent);
                        break;

                    default:
                        return true;
                }
                return false;
            }
        });

        popup.show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(services.this, MainActivity.class);
        startActivity(intent);
    }
}
