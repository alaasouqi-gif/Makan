package com.example.makan.activity.add_services;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.example.makan.activity.services;
import com.example.makan.adapter.adapter_image;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class add_gallery extends AppCompatActivity {
    Toolbar toolbar;

    private static final int PICK_IMAGE_REQUEST = 1;


    Uri coverPhoto;
    AppCompatButton button;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    //private PhotoAdapter photoAdapter;
    adapter_image adapter;
    private TedBottomPicker tedBottomPicker;

    ArrayList<Uri> uploadArray = new ArrayList<>();

    String key;
    RelativeLayout relativeLayout;

    String s;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gallery);

        button = findViewById(R.id.Next);
        progressBar = findViewById(R.id.progressBar2);
        relativeLayout = findViewById(R.id.all);


        key = getIntent().getStringExtra("serviceId");

        storageReference = FirebaseStorage.getInstance().getReference("services").child(key).child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("services").child(key).child("images");

        if (getIntent().getStringExtra("type1").equals("restaurant") || getIntent().getStringExtra("type1").equals("shopping")) {
            button.setText("Next");
        } else {
            button.setText("Finish");
        }

        button.setEnabled(false);

        //photoAdapter=new PhotoAdapter(add_gallery.this);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.gallery_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new adapter_image(uploadArray, this);

        recyclerView.setAdapter(adapter);


        findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomPicker();
            }
        });

        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = getIntent().getStringExtra("image1");
                Log.d("here", "onCreate: " + s);
                coverPhoto = Uri.parse(s);

                Bundle extras = getIntent().getExtras();
                String type = extras.getString("type1");
                if (type.equals("restaurant") || type.equals("store")) {
                    Intent intent = new Intent(add_gallery.this, add_menu.class);
                    if (!uploadArray.isEmpty()) {
                        String[] imgs = new String[uploadArray.size()];
                        for (int i = 0; i < uploadArray.size(); i++) {
                            imgs[i] = uploadArray.get(i).toString();
                        }
                        intent.putExtra("imgs", imgs);
                    }
                    intent.putExtra("lat1", getIntent().getDoubleExtra("lat", 33.33));
                    intent.putExtra("lng1", getIntent().getDoubleExtra("lng", 33.33));
                    intent.putExtra("city1", getIntent().getStringExtra("city"));
                    intent.putExtra("owner2", getIntent().getStringExtra("owner1"));
                    intent.putExtra("name2", getIntent().getStringExtra("name1"));
                    intent.putExtra("categories2", getIntent().getStringArrayExtra("categories1"));
                    intent.putExtra("open2", getIntent().getStringExtra("open1"));
                    intent.putExtra("close2", getIntent().getStringExtra("close1"));
                    intent.putExtra("off2", getIntent().getStringArrayExtra("off1"));
                    intent.putExtra("max2", getIntent().getStringExtra("max1"));
                    intent.putExtra("min2", getIntent().getStringExtra("min1"));
                    intent.putExtra("price2", getIntent().getStringExtra("price1"));
                    intent.putExtra("cancel2", getIntent().getStringExtra("cancel1"));
                    intent.putExtra("phone2", getIntent().getStringExtra("phone1"));
                    intent.putExtra("email2", getIntent().getStringExtra("email1"));
                    intent.putExtra("description2", getIntent().getStringExtra("description1"));
                    intent.putExtra("image2", getIntent().getStringExtra("image1"));
                    intent.putExtra("key2", getIntent().getStringExtra("serviceId"));
                    intent.putExtra("type2", getIntent().getStringExtra("type1"));

                    startActivity(intent);
                    finish();

                } else if (type.equals("event")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(add_gallery.this, R.style.Theme_MaterialComponents_Light_Dialog);
                    builder.setTitle("Are you sure you want to cancel adding place?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressBar.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.GONE);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(key);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("services").child(key).setValue(key);


                            final double lat = getIntent().getDoubleExtra("lat", 33.33);
                            final double lng = getIntent().getDoubleExtra("lng", 33.33);
                            final String city = getIntent().getStringExtra("city");
                            final String name = getIntent().getStringExtra("name1");
                            final String[] cate = getIntent().getStringArrayExtra("categories1");
                            final String open = getIntent().getStringExtra("open1");
                            final String close = getIntent().getStringExtra("close1");
                            final String price1 = getIntent().getStringExtra("price1");
                            final String phone = getIntent().getStringExtra("phone1");
                            final String email = getIntent().getStringExtra("email1");
                            final String des = getIntent().getStringExtra("description1");
                            final String type11 = getIntent().getStringExtra("type1");


                            Event uploadRes = new Event(
                                    lat, lng, city, name, open, close, price1, phone, email, des
                            );

                            reference.setValue(uploadRes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadCover(coverPhoto);
                                }
                            });

                            reference.child("books").setValue(0);
                            reference.child("rate").setValue("0");
                            reference.child("visits").setValue(0);
                            reference.child("tickets").setValue(0);
                            reference.child("type").setValue(type11);
                            reference.child("dislike").setValue(0);
                            reference.child("interested").setValue(0);
                            reference.child("like").setValue(0);
                            reference.child("tickets num").setValue(getIntent().getStringExtra("tickets_number1"));

                            int J = 0;
                            for (String s : cate) {
                                reference.child("categories").child("" + J++).setValue(s);
                            }

                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else if (type.equals("gym")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(add_gallery.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                    builder.setTitle("Are you sure from the data you entered?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(key);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("services").child(key).setValue(key);

                            final double lat = getIntent().getDoubleExtra("lat", 33.33);
                            final double lng = getIntent().getDoubleExtra("lng", 33.33);
                            final String city = getIntent().getStringExtra("city");
                            final String name = getIntent().getStringExtra("name1");
                            final String open = getIntent().getStringExtra("open1");
                            final String close = getIntent().getStringExtra("close1");
                            final String phone = getIntent().getStringExtra("phone1");
                            final String email = getIntent().getStringExtra("email1");
                            final String des = getIntent().getStringExtra("description1");
                            final String type11 = getIntent().getStringExtra("type1");
                            final String[] off = getIntent().getStringArrayExtra("off1");


                            Gym uploadRes = new Gym(
                                    lat, lng, city, name, open, close, phone, email, des
                            );

                            reference.setValue(uploadRes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadCover(coverPhoto);
                                }
                            });

                            ArrayList<Integer> per = getIntent().getIntegerArrayListExtra("periods1");
                            ArrayList<Integer> pri = getIntent().getIntegerArrayListExtra("prices1");


                            for (int v = 0; v < per.size(); v++) {
                                reference.child("subscription type").child((v + 1) + "").setValue(pri.get(v));
                            }

                            reference.child("books").setValue(0);
                            reference.child("rate").setValue("0");
                            reference.child("visits").setValue(0);
                            reference.child("type").setValue(type11);
                            reference.child("categories").child(0 + "").setValue("Gym");
                            reference.child("like").setValue(0);
                            reference.child("dislike").setValue(0);

                            int i22 = 0;
                            for (String s : off) {
                                reference.child("off days").child(i22 + "").setValue(s);
                                i22++;
                            }

                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else if (type.equals("pitch")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(add_gallery.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                    builder.setTitle("Are you sure you want to cancel adding place?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services").child(key);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("services").child(key).setValue(key);

                            final double lat = getIntent().getDoubleExtra("lat", 33.33);
                            final double lng = getIntent().getDoubleExtra("lng", 33.33);
                            final String city = getIntent().getStringExtra("city");
                            final String name = getIntent().getStringExtra("name1");
                            final String open = getIntent().getStringExtra("open1");
                            final String close = getIntent().getStringExtra("close1");
                            final String phone = getIntent().getStringExtra("phone1");
                            final String email = getIntent().getStringExtra("email1");
                            final String des = getIntent().getStringExtra("description1");
                            final String type11 = getIntent().getStringExtra("type1");
                            final String[] off = getIntent().getStringArrayExtra("off1");


                            Pitch uploadRes = new Pitch(
                                    lat, lng, city, name, open, close, phone, email, des
                            );

                            reference.setValue(uploadRes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadCover(coverPhoto);
                                }
                            });

                            reference.child("subscription type").child("1").setValue(getIntent().getStringExtra("601"));
                            reference.child("subscription type").child("2").setValue(getIntent().getStringExtra("1201"));

                            reference.child("books").setValue(0);
                            reference.child("rate").setValue("0");
                            reference.child("visits").setValue(0);
                            reference.child("type").setValue(type11);
                            reference.child("categories").child(0 + "").setValue("Football");
                            reference.child("like").setValue(0);
                            reference.child("dislike").setValue(0);

                            int i22 = 0;
                            for (String s : off) {
                                reference.child("off days").child(i22 + "").setValue(s);
                                i22++;
                            }

                        }
                    })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            }
        });


    }


    private void openBottomPicker() {
        TedBottomPicker.OnMultiImageSelectedListener listener = new TedBottomPicker.OnMultiImageSelectedListener() {
            @Override
            public void onImagesSelected(ArrayList<Uri> uriList) {
                if (!uriList.isEmpty()) {
                    uploadArray.addAll(uriList);
                    adapter.notifyDataSetChanged();
                    button.setEnabled(true);
                }
            }
        };

        tedBottomPicker = new TedBottomPicker.Builder(this)
                .setOnMultiImageSelectedListener(listener)
                .setCompleteButtonText("DONE")
                .setEmptySelectionText("Please Select Images")
                .create();

        tedBottomPicker.show(getSupportFragmentManager());

    }


    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    StorageReference storageReference1;
    DatabaseReference databaseReference1;
    int count = 0;

    DatabaseReference ref;

    StorageReference reference12;

    private void uploadCover(Uri coverPhoto) {

        Toast.makeText(this, coverPhoto.getPath(), Toast.LENGTH_SHORT).show();

        storageReference = FirebaseStorage.getInstance().getReference("services").child(key).child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("services").child(key).child("cover photo");
        reference12 = storageReference.child("coverPhoto.getLastPathSegment()");
        UploadTask uploadTask;
        uploadTask = reference12.putFile(coverPhoto);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return reference12.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    databaseReference.setValue(downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(final Void aVoid) {

                            for (Uri u : uploadArray) {
                                uploadFiles(u);
                            }
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERRORR IS", "onFailure: " + e.getMessage());
            }
        });


    }

    int count2 = 0;

    private void uploadFiles(Uri uri) {
        Log.d("image", "onSuccess: image uploaded");

        storageReference = FirebaseStorage.getInstance().getReference("services").child(key).child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("services").child(key).child("images");
        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "");
        UploadTask uploadTask;
        uploadTask = reference.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("image", "onSuccess: image uploaded");


            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    databaseReference.push().setValue(downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            count2++;
                            if (count2 == uploadArray.size()) {
                                Intent intent = new Intent(add_gallery.this, services.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                }
            }
        });

    }

}