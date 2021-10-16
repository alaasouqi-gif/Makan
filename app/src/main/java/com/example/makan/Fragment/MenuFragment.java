package com.example.makan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.makan.R;
import com.example.makan.activity.Log_in;
import com.example.makan.activity.Top_services;
import com.example.makan.activity.choose_interest;
import com.example.makan.activity.list.list_Reservations;
import com.example.makan.activity.list.list_check_later;
import com.example.makan.activity.my_profile;
import com.example.makan.activity.services;
import com.example.makan.activity.sign_up;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MenuFragment extends Fragment {

    Log_in log_in = new Log_in();
    sign_up sign_up = new sign_up();

    public MenuFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView reservation;
        TextView statistics;
        TextView services;
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        reservation = root.findViewById(R.id.reservation);
        statistics = root.findViewById(R.id.statistics);
        services = root.findViewById(R.id.services);

        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), list_Reservations.class);
                startActivity(intent);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Top_services.class);
                startActivity(intent);
            }
        });

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), services.class);
                startActivity(intent);
            }
        });


        root.findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getContext(), Log_in.class);
                log_in.getFirebaseAuth().signOut();
                sign_up.getmAuth().getInstance().signOut();

                log_in.getGoogleSignInClient().signOut().addOnCompleteListener(Objects.requireNonNull(getActivity()),
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(intent);
                            }
                        });
            }
        });
        root.findViewById(R.id.interests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), choose_interest.class);
                startActivity(intent);
            }
        });

        root.findViewById(R.id.check_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), list_check_later.class);
                startActivity(intent);

            }
        });

        root.findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), my_profile.class);
                startActivity(intent);

            }
        });

        return root;
    }
}
