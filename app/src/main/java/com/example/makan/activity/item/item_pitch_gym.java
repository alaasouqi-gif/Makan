package com.example.makan.activity.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.example.makan.activity.list.list_monitor;
import com.example.makan.activity.offers;
import com.example.makan.activity.payment.payment_gym;
import com.example.makan.activity.payment.payment_pitch;
import com.example.makan.adapter.adapter_gallary_show;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;


public class item_pitch_gym extends AppCompatActivity {
    Toolbar toolbar;

    TextView name;
    TextView start_date;
    TextView end_date;
    TextView location;
    TextView price;
    TextView description;
    TextView email;
    TextView phone;
    TextView rate;
    Button monitor;
    Button offer;
    Long visits;


    AppCompatImageView imageView;
    RecyclerView recyclerView;
    String serviceId;
    String serviceType;
    ImageView check_later;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid()).child("check later");
    DatabaseReference user_ref_like = database.getReference("Users").child(user.getUid());
    DatabaseReference current_user = database.getReference("Users").child(user.getUid());

    int likeflag;
    ImageView like;
    ImageView dis;
    Button Ticket;
    LinearLayout linearLayout;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pitch_gym);

        name = findViewById(R.id.name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        location = findViewById(R.id.location);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        imageView = findViewById(R.id.imageView);
        rate = findViewById(R.id.rate);
        check_later = findViewById(R.id.check_later);
        like = findViewById(R.id.like);
        dis = findViewById(R.id.dis);
        Ticket = findViewById(R.id.Tickets);
        linearLayout = findViewById(R.id.add_to);
        offer = findViewById(R.id.offer);
        monitor = findViewById(R.id.monitor);


        recyclerView = findViewById(R.id.recyclerview_gallary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Intent intent = getIntent();
        serviceId = intent.getStringExtra("id");
        serviceType = intent.getStringExtra("type");

        flag = 0;
        likeflag = 0;

        toolbar = findViewById(R.id.toolbar);
        if (serviceType.equals("gym")) {
            toolbar.setTitle("Gym");

        } else {
            toolbar.setTitle("Pitch");
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

                    if (serviceType.equals("gym")) {
                        collapsingToolbarLayout.setTitle("Gym");

                    } else {
                        collapsingToolbarLayout.setTitle("Pitch");
                    }
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    isShow = false;
                }
            }
        });


        current_user.child("services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()) {

                    if (Objects.equals(s.getValue(), serviceId)) {

                        Ticket.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.GONE);
                        offer.setVisibility(View.VISIBLE);
                        monitor.setVisibility(View.VISIBLE);

                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        countVisits();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(serviceId);


        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name.setText(snapshot.child("name").getValue().toString());
                Picasso.get().load(snapshot.child("cover photo").getValue().toString()).into(imageView);

                start_date.setText("Open : " + snapshot.child("open").getValue());
                end_date.setText("Close : " + snapshot.child("close").getValue());
                description.setText(snapshot.child("des").getValue() + "");
                phone.setText("Phone " + snapshot.child("phone").getValue());
                email.setText("Email : " + snapshot.child("email").getValue());
                location.setText(snapshot.child("city").getValue() + "");
                rate.setText(snapshot.child("rate").getValue().toString().substring(0, Math.min(3, snapshot.child("rate").getValue().toString().length())) + " based on " + (Integer.parseInt(snapshot.child("like").getValue().toString()) + Integer.parseInt(snapshot.child("dislike").getValue().toString())) + " Review");

                ArrayList<String> i = new ArrayList<>();
                for (DataSnapshot image : snapshot.child("images").getChildren()) {
                    i.add(image.getValue().toString());
                }
                adapter_gallary_show adapter = new adapter_gallary_show(i, getApplicationContext());
                recyclerView.setAdapter(adapter);

                user_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot date_check : dataSnapshot.getChildren()) {

                            if (date_check.getValue().equals(serviceId)) {
                                flag = 1;
                                check_later.setImageResource(R.drawable.history_blue);
                                return;

                            } else {
                                flag = 0;
                                check_later.setImageResource(R.drawable.history_32);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                user_ref_like.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        likeflag = 0;
                        for (DataSnapshot date_check : dataSnapshot.child("Liked").getChildren()) {

                            if (date_check.getValue().equals(serviceId)) {
                                likeflag = 1;
                                like.setImageResource(R.drawable.thumb_up_blue);
                                return;
                            }

                        }

                        for (DataSnapshot date_check : dataSnapshot.child("Disliked").getChildren()) {

                            if (date_check.getValue().equals(serviceId)) {
                                likeflag = 2;
                                dis.setImageResource(R.drawable.thumb_down_blue);
                                return;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (serviceType) {

                    case "pitch":
                        intent = new Intent(item_pitch_gym.this, payment_pitch.class);
                        intent.putExtra("SID", serviceId);
                        startActivity(intent);
                        break;

                    case "gym":
                        intent = new Intent(item_pitch_gym.this, payment_gym.class);
                        intent.putExtra("SID", serviceId);
                        startActivity(intent);
                        break;

                    default:
                }

            }
        });

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(item_pitch_gym.this, offers.class);
                intent.putExtra("SID", serviceId);
                intent.putExtra("type", serviceType);
                startActivity(intent);

            }
        });

        check_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    check_later.setImageResource(R.drawable.history_blue);
                    flag = 1;
                    user_ref.child(serviceId).setValue(serviceId);
                } else {
                    flag = 0;
                    check_later.setImageResource(R.drawable.history_32);
                    user_ref.child(serviceId).removeValue();

                }
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_count = Integer.parseInt(dataSnapshot.child("like").getValue().toString());
                        int dislike_count = Integer.parseInt(dataSnapshot.child("dislike").getValue().toString());

                        if (likeflag == 0) {
                            like.setImageResource(R.drawable.thumb_up_blue);
                            likeflag = 1;
                            user_ref_like.child("Liked").child(serviceId).setValue(serviceId);
                            like_count++;


                        } else if (likeflag == 1) {
                            user_ref_like.child("Liked").child(serviceId).removeValue();
                            likeflag = 0;
                            like.setImageResource(R.drawable.thumb_up);
                            like_count--;

                        } else if (likeflag == 2) {
                            user_ref_like.child("Disliked").child(serviceId).removeValue();

                            likeflag = 1;
                            dis.setImageResource(R.drawable.thumb_down);
                            like.setImageResource(R.drawable.thumb_up_blue);
                            user_ref_like.child("Liked").child(serviceId).setValue(serviceId);
                            like_count++;
                            dislike_count--;


                        }
                        myRef.child("like").setValue(like_count);
                        myRef.child("dislike").setValue(dislike_count);

                        if ((like_count + dislike_count) == 0)
                            myRef.child("rate").setValue(0);
                        else
                            myRef.child("rate").setValue((like_count * 1.0 / (like_count + dislike_count)) * 5);


                    }
                });
                dis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_count = Integer.parseInt(dataSnapshot.child("like").getValue().toString());
                        int dislike_count = Integer.parseInt(dataSnapshot.child("dislike").getValue().toString());

                        if (likeflag == 0) {

                            likeflag = 2;
                            dis.setImageResource(R.drawable.thumb_down_blue);
                            user_ref_like.child("Disliked").child(serviceId).setValue(serviceId);
                            dislike_count++;

                        } else if (likeflag == 1) {
                            user_ref_like.child("Liked").child(serviceId).removeValue();

                            likeflag = 2;
                            dislike_count++;
                            like_count--;


                            like.setImageResource(R.drawable.thumb_up);
                            dis.setImageResource(R.drawable.thumb_down_blue);

                            user_ref_like.child("Disliked").child(serviceId).setValue(serviceId);


                        } else if (likeflag == 2) {
                            user_ref_like.child("Disliked").child(serviceId).removeValue();
                            likeflag = 0;
                            dis.setImageResource(R.drawable.thumb_down);
                            dislike_count--;

                        }
                        myRef.child("like").setValue(like_count);
                        myRef.child("dislike").setValue(dislike_count);
                        if ((like_count + dislike_count) == 0)
                            myRef.child("rate").setValue(0);
                        else
                            myRef.child("rate").setValue((like_count * 1.0 / (like_count + dislike_count)) * 5);

                    }

                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(item_pitch_gym.this, list_monitor.class);
                intent.putExtra("sid", serviceId);
                intent.putExtra("type", serviceType);
                startActivity(intent);
            }
        });


    }

    public void countVisits() {
        final DatabaseReference service = FirebaseDatabase.getInstance().getReference("services").child(serviceId);
        final DatabaseReference visitors = FirebaseDatabase.getInstance().getReference("services").child(serviceId).child("visitors");
        try {
            visitors.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            return;
                        }
                    }
                    visitors.push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    service.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            visits = (Long) snapshot.child("visits").getValue();
                            visits++;
                            service.child("visits").setValue(visits);
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
        } catch (NullPointerException e) {
            visitors.push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            service.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    visits = (Long) snapshot.child("visits").getValue();
                    visits++;
                    service.child("visits").setValue(visits);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


}
