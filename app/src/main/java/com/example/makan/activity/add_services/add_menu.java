package com.example.makan.activity.add_services;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.MenuItem;
import com.example.makan.R;
import com.example.makan.activity.services;
import com.example.makan.adapter.adapter_food;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class add_menu extends AppCompatActivity {

    RecyclerView recyclerView;
    Button add;
    Button Next;
    Button finish;

    TextInputEditText Name;
    TextInputEditText price;
    TextInputEditText category;
    String categoryName;
    String NameItem;
    String PriceItem;

    List<List<MenuItem>> categories = new ArrayList<>();
    List<String> catStrings = new ArrayList<>();

    List<Uri> images = new ArrayList<>();

    DatabaseReference reference;


    AppCompatImageView imageView;

    String key;

    Uri coverPhoto;

    Uri itemPhoto;

    StorageReference reference3;
    int i = 0;


    RecyclerView.Adapter adapter;

    public ArrayList<MenuItem> menuItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);


        final String[] imgs = getIntent().getStringArrayExtra("imgs");
        final double lat = getIntent().getDoubleExtra("lat1", 33.33);
        final double lng = getIntent().getDoubleExtra("lng1", 33.33);
        final String city = getIntent().getStringExtra("city1");
        final String name = getIntent().getStringExtra("name2");
        final String[] cate = getIntent().getStringArrayExtra("categories2");
        final String open = getIntent().getStringExtra("open2");
        final String close = getIntent().getStringExtra("close2");
        final String[] off = getIntent().getStringArrayExtra("off2");
        final String max = getIntent().getStringExtra("max2");
        final String min = getIntent().getStringExtra("min2");
        final String price1 = getIntent().getStringExtra("price2");
        final String cancel = getIntent().getStringExtra("cancel2");
        final String phone = getIntent().getStringExtra("phone2");
        final String email = getIntent().getStringExtra("email2");
        final String des = getIntent().getStringExtra("description2");
        final String type = getIntent().getStringExtra("type2");

        coverPhoto = Uri.parse(getIntent().getStringExtra("image2"));
        for (String s : imgs) {
            images.add(Uri.parse(s));
        }

        key = getIntent().getStringExtra("key2");

        Toolbar toolbar = findViewById(R.id.toolbar);

        imageView = findViewById(R.id.image);

        add = findViewById(R.id.add);
        Name = findViewById(R.id.item_name);
        price = findViewById(R.id.price);

        Next = findViewById(R.id.Next);
        finish = findViewById(R.id.Finish);
        category = findViewById(R.id.category_name);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.food_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new adapter_food(menuItem, this);

        recyclerView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NameItem = Name.getText().toString();

                if (NameItem.equals("")) {
                    Name.setError("Enter Item Name");
                    Name.findFocus();
                }

                try {


                    PriceItem = String.valueOf(Integer.parseInt(price.getText().toString()));


                    if (NameItem.equals("")) {
                        Name.setError("Enter Item Name");
                        Name.findFocus();
                    }

                    if (!(NameItem.equals("") || PriceItem.equals(""))) {

                        if (itemPhoto == null) {
                            Toast.makeText(add_menu.this, "No URI", Toast.LENGTH_SHORT).show();
                        } else {
                            menuItem.add(new MenuItem(NameItem, PriceItem + " JD", itemPhoto, category.getText().toString()));
                            adapter.notifyDataSetChanged();

                            Name.getText().clear();
                            price.getText().clear();
                            //category.getText().clear();
                        }


                    }

                } catch (NumberFormatException e) {
                    price.setError("Enter category name");
                    price.findFocus();

                    e.printStackTrace();
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        itemPhoto = uri;
                        imageView.setImageURI(uri);
                    }
                };

                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(add_menu.this)
                        .setOnImageSelectedListener(listener)
                        .create();
                tedBottomPicker.show(getSupportFragmentManager());
            }
        });


        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryName = category.getText().toString();
                if (categoryName.equals("") && Name.getText().toString().equals("") && price.getText().toString().equals("")) {
                    category.setError("Enter category name");
                    Name.setError("Enter item name");
                    price.setError("Enter item price");
                    category.findFocus();
                } else {
                    catStrings.add(category.getText().toString());
                    category.getText().clear();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_menu.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
                builder.setTitle("Are you sure you want to cancel adding place?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!category.getText().toString().equals("")) {
                            catStrings.add(category.getText().toString());
                        }

                        catStrings.add(category.getText().toString());

                        reference = FirebaseDatabase.getInstance().getReference("services").child(key);

                        if (getIntent().getStringExtra("type2").equals("restaurant")) {
                            Restaurant uploadRes = new Restaurant(
                                    lat, lng, city, name, open, close, max, min, price1, cancel, phone, email, des
                            );

                            reference.setValue(uploadRes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadCover(coverPhoto);
                                }
                            });
                        } else if (getIntent().getStringExtra("type2").equals("store")) {
                            Store uploadRes = new Store(
                                    lat, lng, city, name, open, close, phone, email, des
                            );

                            reference.setValue(uploadRes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadCover(coverPhoto);
                                }
                            });
                        }


                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("services").child(key).setValue(key);

                        reference.child("books").setValue(0);
                        reference.child("rate").setValue("0");
                        reference.child("visits").setValue(0);
                        reference.child("like").setValue(0);
                        reference.child("dislike").setValue(0);
                        reference.child("type").setValue(getIntent().getStringExtra("type2"));
                        int in = 0;

                        for (String s : cate) {
                            reference.child("categories").child(in + "").setValue(s);
                            in++;
                        }
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
        });
    }

    StorageReference storageReference;
    DatabaseReference databaseReference;
    int count = 0;

    DatabaseReference ref;

    StorageReference reference12;

    private void uploadCover(Uri coverPhoto) {

        storageReference = FirebaseStorage.getInstance().getReference("services").child(key).child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("services").child(key).child("cover photo");
        reference12 = storageReference.child(coverPhoto.getLastPathSegment());
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
                            ref = FirebaseDatabase.getInstance().getReference("services").child(key).child("menu");

                            for (int i = 0; i < catStrings.size(); i++) {
                                for (MenuItem menuItem1 : menuItem) {
                                    if (menuItem1.category.equals(catStrings.get(i))) {

                                        reference3 = storageReference.child(menuItem1.getImage().getLastPathSegment());

                                        UploadTask uploadTask1 = reference3.putFile(menuItem1.getImage());
                                        final int finalI = i;
                                        final MenuItem finalitem = menuItem1;
                                        Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (!task.isSuccessful()) {
                                                    Log.d("ERRORR", "then: " + task.getException());
                                                    throw task.getException();
                                                }

                                                // Continue with the task to get the download URL
                                                return reference3.getDownloadUrl();
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    Uri downloadUri = task.getResult();
                                                    ref.child(finalitem.category).child(finalitem.getName()).child("price").setValue(finalitem.getPrice());
                                                    ref.child(finalitem.category).child(finalitem.getName()).child("photo").setValue(downloadUri.toString());
                                                    count++;
                                                    if (count == menuItem.size() - 1) {
                                                        Toast.makeText(add_menu.this, "inif", Toast.LENGTH_SHORT).show();
                                                        for (Uri u : images) {
                                                            uploadFiles(u);
                                                        }
                                                    }


                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("failed", "onFailure: " + e.getMessage());
                                                Toast.makeText(add_menu.this, "failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                }
                            }


                        }
                    });

                }
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
                            if (count2 == images.size() - 1) {
                                Intent intent = new Intent(add_menu.this, services.class);
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
