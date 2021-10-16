package com.example.makan.activity.edit;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.R;
import com.example.makan.adapter.adapter_image;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class edit_gallery extends AppCompatActivity {
    Toolbar toolbar;

    private static final int PICK_IMAGE_REQUEST = 1;


    Uri coverPhoto;
    AppCompatButton button;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    adapter_image adapter;

    ArrayList<Uri> uploadArray = new ArrayList<>();

    String key;

    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gallery);

        button = findViewById(R.id.Next);

        key = getIntent().getStringExtra("serviceId");

        storageReference = FirebaseStorage.getInstance().getReference("services").child(key).child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("services").child(key).child("images");


        button.setEnabled(false);


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

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()) {
                    uploadArray.add(Uri.parse(d.getValue().toString()) );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        adapter = new adapter_image(uploadArray, edit_gallery.this);

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

                //databaseReference.removeValue();
                for (int i = 0; i < uploadArray.size(); i++) {
                    uploadFiles(uploadArray.get(i));

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

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(this)
                .setOnMultiImageSelectedListener(listener)
                .setCompleteButtonText("DONE")
                .setEmptySelectionText("Please Select Images")
                .create();

        tedBottomPicker.show(getSupportFragmentManager());

    }

    int c = 0;


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
                            c++;
                            if (c == uploadArray.size()) {
                                finish();
                            }

                        }
                    });

                }
            }
        });

    }

}