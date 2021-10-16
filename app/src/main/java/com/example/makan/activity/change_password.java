package com.example.makan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class change_password extends AppCompatActivity {

    TextInputEditText last;
    TextInputEditText pass;
    TextInputEditText pass2;


    MaterialButton signUp;

    Toolbar toolbar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        pass = findViewById(R.id.password);

        pass2 = findViewById(R.id.c_password);

        signUp = findViewById(R.id.sign_up);

        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checker = true;
                final String password = pass.getText().toString();
                final String password2 = pass2.getText().toString();
                final String lasts = last.getText().toString();


                if (!password.equals("")) {
                    if (password.equals(password2)) {

                        if (password.length() < 6) {
                            Toast.makeText(change_password.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                            pass.setError("Password must be at least 6 characters");
                            pass.findFocus();
                            checker = false;
                        }

                    } else if (pass2.getText().toString().equals("")) {
                        checker = false;
                        pass2.setError("Please Enter Confirm Password");
                    } else {
                        checker = false;
                        pass2.setError("Passwords Do Not Match");
                    }

                    if (checker) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        user.updatePassword(password)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            finish();
                                        }
                                    }
                                });

                    }
                } else {
                    checker = false;
                    pass.setError("Please Enter Password");
                }

            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }


}
