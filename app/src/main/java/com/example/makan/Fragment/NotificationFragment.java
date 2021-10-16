package com.example.makan.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makan.Data.notification;
import com.example.makan.R;
import com.example.makan.adapter.adapter_notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private ArrayList<notification> notifications = new ArrayList<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user_ref = database.getReference("Users").child(user.getUid()).child("notified offers");
    DatabaseReference notification_ref = database.getReference("services");
    RecyclerView recyclerView;

    public NotificationFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_not, container, false);
        recyclerView = root.findViewById(R.id.notification_List);

        String image;
        notification_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot1) {

                user_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        notifications.clear();
                        try {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                notifications.add(0, new notification(
                                        snapshot.child("title").getValue().toString()
                                        , snapshot.child("description").getValue().toString()
                                        , (dataSnapshot1.child(snapshot.child("service").getValue().toString())).child("cover photo").getValue().toString()
                                        , snapshot.child("service").getValue().toString()
                                        , (dataSnapshot1.child(snapshot.child("service").getValue().toString())).child("type").getValue().toString()
                                        , snapshot.getKey()
                                        , snapshot.child("open").getValue().toString()

                                ));
                            }
                        } catch (Exception e) {

                        }

                        adapter_notifications adapter = new adapter_notifications(notifications, getActivity());

                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return root;
    }
}
