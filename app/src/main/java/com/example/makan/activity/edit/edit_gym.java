package com.example.makan.activity.edit;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.example.makan.activity.list.list_monitor;
import com.example.makan.activity.offers;
import com.example.makan.adapter.edit_g;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class edit_gym extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    String serviceId;
    String serviceType;
    int StartHour;
    int StartMinute;
    Double lat;
    Double lng;
    TextView name;
    TextView start_date;
    TextView end_date;
    TextView location;
    TextView description;
    TextView email;
    TextView phone;
    TextView rate;
    TextView off_day;
    TextView edit_gallery;
    Button offer;
    Button monitor;
    Button edit;

    ArrayList<String> off_days = new ArrayList<>();

    LinearLayout linearLayout;

    AppCompatImageView imageView;
    RecyclerView recyclerView;


    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gym);

        flag = 0;

        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        imageView = findViewById(R.id.imageView);
        rate = findViewById(R.id.rate);
        linearLayout = findViewById(R.id.add_to);
        off_day = findViewById(R.id.off_day);
        edit_gallery = findViewById(R.id.edit_gallery);
        offer = findViewById(R.id.offer);
        monitor = findViewById(R.id.monitor);
        edit = findViewById(R.id.edit);
        Intent intent = getIntent();
        serviceId = intent.getStringExtra("id");
        serviceType = intent.getStringExtra("type");

        toolbar.setTitle("Store");
        setSupportActionBar(toolbar);
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
                    collapsingToolbarLayout.setTitle("Gym");

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
                    isShow = false;
                }
            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(serviceId);

        recyclerView = findViewById(R.id.recyclerview_gallary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                lat = (Double) snapshot.child("lat").getValue();
                lng = (Double) snapshot.child("lng").getValue();

                name.setText(snapshot.child("name").getValue().toString());
                Picasso.get().load(snapshot.child("cover photo").getValue().toString()).into(imageView);

                start_date.setText("Open : " + snapshot.child("open").getValue());
                end_date.setText("Close : " + snapshot.child("close").getValue());

                description.setText(snapshot.child("des").getValue() + "");
                phone.setText("Phone : " + snapshot.child("phone").getValue());
                email.setText("Email : " + snapshot.child("email").getValue());
                location.setText(snapshot.child("city").getValue() + "");
                rate.setText(snapshot.child("rate").getValue().toString().substring(0, Math.min(4, snapshot.child("rate").getValue().toString().length())) + " based on " + (Integer.parseInt(snapshot.child("like").getValue().toString()) + Integer.parseInt(snapshot.child("dislike").getValue().toString())) + " Review");

                try {
                    off_days.add(snapshot.child("off days").child(0 + "").getValue().toString());
                    off_day.setText("Off Days : " + off_days.get(0));

                    for (int J = 1; J < snapshot.child("off days").getChildrenCount(); J++) {
                        off_days.add(snapshot.child("off days").child(J + "").getValue().toString());
                        off_day.setText(off_day.getText().toString() + ", " + off_days.get(J));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList<String> i = new ArrayList<>();
                ArrayList<String> i2 = new ArrayList<>();
                for (DataSnapshot image : snapshot.child("images").getChildren()) {
                    i.add(image.getValue().toString());
                    i2.add(image.getKey());
                }
                edit_g adapter = new edit_g(i, edit_gym.this, i2, serviceId);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                intent = new Intent(edit_gym.this, offers.class);
                intent.putExtra("SID", serviceId);
                intent.putExtra("type", serviceType);
                startActivity(intent);

            }
        });
        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(edit_gym.this, list_monitor.class);
                intent.putExtra("sid", serviceId);
                intent.putExtra("type", serviceType);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(edit_gym.this, edit_gym_reservation.class);
                intent1.putExtra("sid", serviceId);
                startActivity(intent1);
            }
        });
        findViewById(R.id.re).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(serviceId);
            }
        });
        name.setOnClickListener(this);
        start_date.setOnClickListener(this);
        end_date.setOnClickListener(this);
        location.setOnClickListener(this);
        description.setOnClickListener(this);
        email.setOnClickListener(this);
        phone.setOnClickListener(this);
        imageView.setOnClickListener(this);
        off_day.setOnClickListener(this);
        edit_gallery.setOnClickListener(this);

    }

    AlertDialog.Builder builder;
    View custom;
    AlertDialog alert;

    @Override
    public void onClick(View view) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("services").child(serviceId);

        switch (view.getId()) {

            case R.id.name:
                builder = new AlertDialog.Builder(edit_gym.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                custom = getLayoutInflater().inflate(R.layout.dialog_edit, null);
                builder.setView(custom);
                builder.setTitle("Edit Information");

                final TextInputEditText EditText = custom.findViewById(R.id.Event_name);
                EditText.setHint("Gym Name");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child("name").setValue(EditText.getText().toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                break;

            case R.id.email:

                builder = new AlertDialog.Builder(edit_gym.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                custom = getLayoutInflater().inflate(R.layout.dialog_edit, null);
                builder.setView(custom);
                builder.setTitle("Edit Information");

                final TextInputEditText email = custom.findViewById(R.id.Event_name);
                email.setHint("Email");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child("email").setValue(email.getText().toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                break;

            case R.id.phone:
                builder = new AlertDialog.Builder(edit_gym.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                custom = getLayoutInflater().inflate(R.layout.dialog_edit, null);
                builder.setView(custom);
                builder.setTitle("Edit Information");

                final TextInputEditText phone = custom.findViewById(R.id.Event_name);
                phone.setHint("Phone Number");
                phone.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child("phone").setValue(phone.getText().toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                break;

            case R.id.description:
                builder = new AlertDialog.Builder(edit_gym.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                custom = getLayoutInflater().inflate(R.layout.dialog_edit_describtion, null);
                builder.setView(custom);
                builder.setTitle("Edit Information");

                final TextInputEditText des = custom.findViewById(R.id.description);
                des.setText(description.getText());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child("des").setValue(des.getText().toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                break;

            case R.id.off_day:
                builder = new AlertDialog.Builder(edit_gym.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                custom = getLayoutInflater().inflate(R.layout.dialog_multiedittext, null);
                builder.setView(custom);
                builder.setTitle("Edit Information");

                final MultiAutoCompleteTextView off_day1 = custom.findViewById(R.id.off_day);
                String[] item2 = {"No off days", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                off_day1.setHint("Off Days");

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.city_drop_menu, item2);
                off_day1.setAdapter(adapter2);
                off_day1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                off_day1.setAdapter(adapter2);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child("off days").removeValue();

                        final String offDays = off_day1.getText().toString();
                        String[] strings = offDays.split(", ");
                        for (int j = 0; j < strings.length; j++) {
                            if (!strings[j].equals("")) {
                                myRef.child("off days").child(j + "").setValue(strings[j]);
                            }
                        }

                        myRef.child("off days").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                off_days.clear();
                                try {
                                    off_days.add(dataSnapshot.child(0 + "").getValue().toString());

                                    off_day.setText("Off Days : " + off_days.get(0));

                                    for (int J = 1; J < dataSnapshot.getChildrenCount(); J++) {
                                        off_days.add(dataSnapshot.child(J + "").getValue().toString());
                                        off_day.setText(off_day.getText().toString() + ", " + off_days.get(J));
                                    }

                                } catch (Exception e) {
                                    off_day.setText("Off Days : ");
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();

                break;

            case R.id.start_date:

                Calendar calendar;

                calendar = Calendar.getInstance();
                StartHour = calendar.get(Calendar.HOUR_OF_DAY);
                StartMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;

                timePickerDialog = new TimePickerDialog(edit_gym.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String hour;
                        String minute;
                        if (hourOfDay < 10) {
                            hour = "0" + hourOfDay;
                        } else {
                            hour = hourOfDay + "";
                        }
                        if (minutes < 10) {
                            minute = "0" + minutes;
                        } else {
                            minute = minutes + "";
                        }
                        myRef.child("open").setValue(hour + ":" + minute);

                    }
                }, StartHour, StartMinute, true);

                timePickerDialog.show();


                break;

            case R.id.end_date:
                Calendar calendar2;

                calendar2 = Calendar.getInstance();
                StartHour = calendar2.get(Calendar.HOUR_OF_DAY);
                StartMinute = calendar2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog2;

                timePickerDialog2 = new TimePickerDialog(edit_gym.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String hour;
                        String minute;
                        if (hourOfDay < 10) {
                            hour = "0" + hourOfDay;
                        } else {
                            hour = hourOfDay + "";
                        }
                        if (minutes < 10) {
                            minute = "0" + minutes;
                        } else {
                            minute = minutes + "";
                        }
                        myRef.child("close").setValue(hour + ":" + minute);

                    }
                }, StartHour, StartMinute, true);

                timePickerDialog2.show();

                break;


            case R.id.edit_gallery:
                Intent intent = new Intent(this, edit_gallery.class);
                intent.putExtra("serviceId", serviceId);
                startActivity(intent);

                break;
            case R.id.location:
                Intent i = new Intent(this, edit_location.class);
                i.putExtra("key", serviceId);
                i.putExtra("lat", lat);
                i.putExtra("lng", lng);
                startActivity(i);

                break;

        }

    }

    public void deleteService(final String serviceId) {
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Visited places").getChildren()) {
                        if (dataSnapshot1.getKey().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("Visited places").child(serviceId).removeValue();
                            break;
                        }
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("notified offers").getChildren()) {
                        if (dataSnapshot1.child("service").getValue().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("notified offers").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        dataSnapshot1.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("reservations").getChildren()) {
                        if (dataSnapshot1.child("service").getValue().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("reservations").child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        dataSnapshot1.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("services").getChildren()) {
                        if (dataSnapshot1.getKey().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("services").child(dataSnapshot1.getKey()).removeValue();
                            break;
                        }
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("check later").getChildren()) {
                        if (dataSnapshot1.getKey().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("check later").child(dataSnapshot1.getKey()).removeValue();
                            break;
                        }
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Liked").getChildren()) {
                        if (dataSnapshot1.getKey().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("Liked").child(dataSnapshot1.getKey()).removeValue();
                            break;
                        }
                    }
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Disliked").getChildren()) {
                        if (dataSnapshot1.getKey().equals(serviceId)) {
                            FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.getKey())
                                    .child("Disliked").child(dataSnapshot1.getKey()).removeValue();
                            break;
                        }
                    }
                }
                DatabaseReference services = FirebaseDatabase.getInstance().getReference("services");
                services.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (final DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.getKey().equals(serviceId)) {
                                FirebaseDatabase.getInstance().getReference("services").child(dataSnapshot.getKey())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                    dataSnapshot1.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                            }
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
        });

    }

}



