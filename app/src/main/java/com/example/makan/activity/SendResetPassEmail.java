package com.example.makan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class SendResetPassEmail extends AppCompatActivity {

    TextInputEditText editText;
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reset_pass_email);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //String email=getIntent().getExtras().getString("emailAdd");

        editText = findViewById(R.id.email);
        textInputLayout = findViewById(R.id.lemail);

        editText.setText(getIntent().getExtras().getString("emailAdd"));

        findViewById(R.id.sent_btn_rst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.useAppLanguage();
                String emailAddress = "";

                String emailAdress = editText.getText().toString();

                if (!emailAdress.equals("")) {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SendResetPassEmail.this, "Check Your Email To Reset Password", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SendResetPassEmail.this, Log_in.class);
                                        startActivity(intent);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Toast.makeText(SendResetPassEmail.this, "There is no account with this email.", Toast.LENGTH_SHORT).show();
                            textInputLayout.setErrorEnabled(true);
                            textInputLayout.setError("No email Found");
                        }
                    });

                } else {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Enter Email");

                }

            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                textInputLayout.setErrorEnabled(false);
                textInputLayout.setError("");
            }
        });

    }
}