package com.example.makan.Fragment;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.makan.R;
import com.example.makan.activity.item.item_Event;
import com.example.makan.activity.item.item_pitch_gym;
import com.example.makan.activity.item.item_restaurant_stores;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GeofireService extends Service {
    private static final String TAG = "!!!!";
    private static final int NOTIFICATION_ID = 112;
    private static final String PRIMARY_CHANNEL_ID = "location_channel";
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    NotificationChannel channel;

    DatabaseReference my_reference = FirebaseDatabase.getInstance().getReference("Users").child(MapsFragment.firebaseUser.getUid()).child("notified offers").push();


    public static final String ACTION_PROCESS_UPDATES = "com.example.myapplication.UPDATE_LOCATION";
    DatabaseReference interestsRef;
    DatabaseReference servicesRef;
    DatabaseReference ref;
    GeoFire geoFire;
    ArrayList<String> interests = new ArrayList<>();
    ArrayList<String> places = new ArrayList<>();
    Double lat = 0.0;
    Double lng = 0.0;
    Boolean aBoolean;
    DatabaseReference reference;
    FusedLocationProviderClient fusedLocationProviderClient;


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && locationResult.getLastLocation() != null) {
                Double latitude = locationResult.getLastLocation().getLatitude();
                Double longitude = locationResult.getLastLocation().getLongitude();
                //toast(latitude+" , "+longitude);
                geoFire.setLocation("Current Location", new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            Log.d("GeofireService", "onCancelled: Error While Updating The Location... ");
                        }
                    }
                });
            }
        }
    };

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //toast("onStartCommand: !!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (intent != null) {

            startLocationService();
        }
        return START_STICKY;
    }

    public void geoQuery() {
        Log.d(TAG, "geoQuery: Start of function" + interests.size());
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (final DataSnapshot loc : snapshot.getChildren()) {
                    try {
                        lat = ((Double) loc.child("lat").getValue());
                        lng = ((Double) loc.child("lng").getValue());

                        for (DataSnapshot cat : loc.child("categories").getChildren()) {
                            if (interests.contains(cat.getValue().toString()) || interests.size() == 0) {
                                geoFire.queryAtLocation(new GeoLocation(lat, lng), 0.5f).addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                                    @Override
                                    public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                                        //toast("Data Entered");
                                        checkVisitedPlaces(loc);
                                    }

                                    @Override
                                    public void onDataExited(DataSnapshot dataSnapshot) {
                                    }

                                    @Override
                                    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
                                    }

                                    @Override
                                    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                                    }

                                    @Override
                                    public void onGeoQueryReady() {
                                    }

                                    @Override
                                    public void onGeoQueryError(DatabaseError error) {
                                    }
                                });
                            }

                        }


                    } catch (NullPointerException e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Log.d(TAG, "geoQuery: End of function");
    }


    private void startLocationService() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(3000);
        locationRequest.setInterval(5000);
        locationRequest.setSmallestDisplacement(0f);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            toast("permissions issue");
            return;
        }


        ref = FirebaseDatabase.getInstance().getReference("Users").child(MapsFragment.firebaseUser.getUid());
        servicesRef = FirebaseDatabase.getInstance().getReference().child("services");
        interestsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(MapsFragment.firebaseUser.getUid()).child("interests");
        geoFire = new GeoFire(ref);
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot place : snapshot.getChildren()) {
                    try {
                        places.add(place.child("name").getValue().toString());
                    } catch (NullPointerException e) {

                    }
                }
                interestsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            for (DataSnapshot interest : snapshot.getChildren()) {
                                interests.add(interest.getValue().toString());
                                Log.d(TAG, "onDataChange: interests of " + MapsFragment.firebaseUser.getUid() + " : " + interest.getValue().toString());
                            }
                        }
                        geoQuery();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("GeofireService", "onCancelled: interests DataSnapshot... ");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("GeofireService", "onCancelled: services DataSnapshot... ");
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void ringTone() {
        /*Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();*/
    }

    private void checkVisitedPlaces(final DataSnapshot placesSnapshot) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(MapsFragment.firebaseUser.getUid()).child("Visited places").child(placesSnapshot.getKey());
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Object o=snapshot.getValue();
                try {
                    String s = snapshot.getValue().toString();
                } catch (NullPointerException e) {
                    createNotification(placesSnapshot.child("name").getValue().toString(), placesSnapshot.child("type").getValue().toString(), placesSnapshot.getKey());
                    // toast(MapsFragment.firebaseUser.getDisplayName() + " entered: " + placesSnapshot.child("name").getValue());
                    DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    String date = format.format(Calendar.getInstance().getTime());
                    reference.setValue(date);

                    my_reference.child("title").setValue(placesSnapshot.child("name").getValue().toString());
                    my_reference.child("description").setValue(placesSnapshot.child("name").getValue() + " is near you maybe its good chosen for you");
                    my_reference.child("service").setValue(placesSnapshot.getKey());
                    my_reference.child("type").setValue("near");
                    my_reference.child("read").setValue("true");
                    my_reference.child("service type").setValue(placesSnapshot.child("type").getValue().toString());
                    my_reference.child("open").setValue("false");

/*
                    child.child("title").setValue(placesSnapshot.child("name"));
                    child.child("description").setValue(placesSnapshot.child("name").getValue()+"is near you maybe its good chosen for you");
                    child.child("service").setValue(placesSnapshot.getKey());
                    child.child("type").setValue("near");
                    child.child("read").setValue("false");
                    child.child("read").setValue("false");
                    child.child("service type").setValue(placesSnapshot.child("type"));
*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reference.addValueEventListener(eventListener);
    }

    public void createNotification(String placeName, String type, String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PRIMARY_CHANNEL_ID, "MESSAGE", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_location_on_black_24dp);
        builder.setContentTitle(placeName);
        builder.setContentText(placeName + " is near you, press to see their page.");
        builder.setTimeoutAfter(50000);

        Log.d(TAG, "createNotification: ssssss" + type);
        switch (type) {
            case "restaurant":
            case "store":
                Intent intent = new Intent(this, item_restaurant_stores.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent);

                break;

            case "event":

                Intent intent2 = new Intent(this, item_Event.class);
                intent2.putExtra("id", id);
                intent2.putExtra("type", type);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(getApplicationContext(), 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent2);
                break;

            case "pitch":
            case "gym":

                Intent intent3 = new Intent(this, item_pitch_gym.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent3.putExtra("id", id);
                intent3.putExtra("type", type);
                PendingIntent pendingIntent3 = PendingIntent.getActivity(getApplicationContext(), 0, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pendingIntent3);

                break;
            default:
                break;


        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(10, builder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d(TAG, "onDestroy: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //toast("onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
