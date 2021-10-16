package com.example.makan.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.makan.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log_in extends AppCompatActivity {
    TextInputEditText email;
    TextInputEditText pass;
    TextView forgot;

    private static final int RC_SIGN_IN = 9002;
    private static FirebaseAuth mAuth;
    private static GoogleSignInClient mGoogleSignInClient;
    MaterialButton singIn;
    com.example.makan.activity.usersData usersData = new usersData();
    TextInputLayout email_l;
    TextInputLayout pass_l;

    DatabaseReference interestRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        try {
            String s = user.getDisplayName();
            if (!user.getEmail().equals("")) {


                Intent to_home = new Intent(this, MainActivity.class);
                startActivity(to_home);


            } else {
                Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();

                setUpUI();
            }
        } catch (NullPointerException e) {
            // Toast.makeText(this, "catch", Toast.LENGTH_SHORT).show();
            setUpUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                updateUI_SIGN_UP(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //   Toast.makeText(this,"firebaseAuthWithGoogle:" + acct.getId(),Toast.LENGTH_SHORT).show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // Toast.makeText(getApplicationContext(),"signInWithCredential:success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI_SIGN_UP(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            updateUI_SIGN_UP(null);
                        }
                    }
                });
    }


    private void updateUI_SIGN_UP(FirebaseUser user) {
        if (user != null) {
            usersData.addUserToDB_GOOGLE(user.getUid(), user.getEmail(), user.getDisplayName());
            Intent to_home = new Intent(this, MainActivity.class);
            startActivity(to_home);
        } else {
            //  Toast.makeText(this, "No User", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI_SIGN_IN(FirebaseUser user) {
        if (user != null) {
            Intent to_home = new Intent(this, MainActivity.class);
            startActivity(to_home);
        }
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean isEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public Boolean isInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public void setUpUI() {
        setContentView(R.layout.activity_log_in);
        singIn = findViewById(R.id.sign_in);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        forgot = findViewById(R.id.forgot_pass);

        email_l = findViewById(R.id.email_l);
        pass_l = findViewById(R.id.pass_l);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        Button sign_up = findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Log_in.this, sign_up.class);
                startActivity(intent);
            }
        });
        singIn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String emailAdress = email.getText().toString();
                                          String password = pass.getText().toString();
                                          if (emailAdress.equals("") && password.equals("")) {
                                              /*email.findFocus();
                                              pass.findFocus();
                                              Toast.makeText(Log_in.this, "Please Enter Your Data", Toast.LENGTH_SHORT).show();*/
                                              email_l.setErrorEnabled(true);
                                              email_l.setError("Enter your email");


                                          } else if (emailAdress.equals("")) {
                                              email_l.setErrorEnabled(true);
                                              email_l.setError("Enter your email");
                                              //Toast.makeText(Log_in.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                                          } else if (!isEmail(emailAdress)) {
                                              email_l.setErrorEnabled(true);
                                              email_l.setError("Invalid Email");
                                              //Toast.makeText(Log_in.this, "Invalid Email", Toast.LENGTH_SHORT).show();

                                          } else if (password.equals("")) {
                                              pass_l.setErrorEnabled(true);
                                              pass_l.setError("Enter Your password");
                                              //Toast.makeText(Log_in.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();

                                          } else {
                                              mAuth.signInWithEmailAndPassword(emailAdress, password)
                                                      .addOnCompleteListener(Log_in.this, new OnCompleteListener<AuthResult>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<AuthResult> task) {
                                                              if (task.isSuccessful()) {
                                                                  // Sign in success, update UI with the signed-in user's information
                                                                  // Toast.makeText(Log_in.this, "signInWithEmail:success", Toast.LENGTH_SHORT).show();
                                                                  FirebaseUser user = mAuth.getCurrentUser();
                                                                  updateUI_SIGN_IN(user);

                                                              } else {
                                                                  try {
                                                                      String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                                                      switch (errorCode) {

                                                                          case "ERROR_WRONG_PASSWORD":
                                                                              //Toast.makeText(Log_in.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                                                              pass_l.setErrorEnabled(true);
                                                                              pass_l.setError("Incorrect password");
                                                                              break;


                                                                          case "ERROR_USER_DISABLED":
                                                                              Toast.makeText(Log_in.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                                                              break;

                                                                          case "ERROR_USER_TOKEN_EXPIRED":
                                                                              Toast.makeText(Log_in.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                                              break;

                                                                          case "ERROR_USER_NOT_FOUND":
                                                                              Toast.makeText(Log_in.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                                                              break;

                                                                          case "ERROR_INVALID_USER_TOKEN":
                                                                              Toast.makeText(Log_in.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                                                              break;

                                                                          default:
                                                                              Toast.makeText(Log_in.this, errorCode, Toast.LENGTH_SHORT).show();

                                                                      }

                                                                  } catch (Exception e) {
                                                                      Toast.makeText(Log_in.this, "Something Wrong!! Wait a second and try again.", Toast.LENGTH_SHORT).show();
                                                                  }


                                                                  updateUI_SIGN_IN(null);
                                                                  // ...
                                                              }

                                                              // ...
                                                          }
                                                      });
                                          }
                                      }

                                  }
        );
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_in.this, SendResetPassEmail.class);
                // if (!email.getText().toString().equals("")) {
                intent.putExtra("emailAdd", email.getText().toString());
                //  }
                startActivity(intent);
            }
        });


    }


}