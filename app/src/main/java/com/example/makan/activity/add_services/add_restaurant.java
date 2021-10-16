package com.example.makan.activity.add_services;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.example.makan.R;
import com.example.makan.activity.services;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gun0912.tedbottompicker.TedBottomPicker;

public class add_restaurant extends AppCompatActivity {

    Toolbar toolbar;
    Button Next;

    int StartHour;
    int StartMinute;
    String Start_amPm;

    int EndHour;
    int EndMinute;
    String End_amPm;
    TextInputEditText StartDate;
    TextInputEditText EndDate;
    TextInputLayout StartDateLayout;
    TextInputLayout EndDateLayout;
    TextInputLayout Location_l;

    TextInputEditText name;

    RadioButton radioButton;
    RadioButton radioButton2;

    TextInputEditText maxTable;

    TextInputEditText maxTable1;

    TextInputEditText price;

    TextInputEditText autoCancel;

    TextInputEditText phoneNum;
    TextInputEditText email;
    TextInputEditText description;

    MultiAutoCompleteTextView type_of_event;
    AutoCompleteTextView cancel_the_reservation;

    List<String> days = new ArrayList<>();
    String daysString = "";

    List<String> cat = new ArrayList<>();
    String catString = "";

    FusedLocationProviderClient fusedLocationProviderClient;

    Uri coverPhoto;

    AppCompatImageView imageView;

    String key;

    DatabaseReference serRef = FirebaseDatabase.getInstance().getReference("services").push();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(add_restaurant.this);

        key = serRef.getKey();

        toolbar = findViewById(R.id.toolbar);
        Next = findViewById(R.id.Next);

        name = findViewById(R.id.name);


        imageView = findViewById(R.id.cover_img);

        price = findViewById(R.id.price);

        phoneNum = findViewById(R.id.phone_number);

        email = findViewById(R.id.email);

        description = findViewById(R.id.description);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        StartDate = findViewById(R.id.start_date);
        StartDateLayout = findViewById(R.id.start_date_layout);

        EndDate = findViewById(R.id.end_date);
        EndDateLayout = findViewById(R.id.end_date_layout);

        StartDateLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetStartDate();
            }
        });

        EndDateLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetEndDate();
            }
        });


        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);

        type_of_event = findViewById(R.id.type_of);
        String[] item = {"Oriental Food", "SeaFood", "Italian Food", "Chinese Food", "FastFood"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.city_drop_menu, item);
        type_of_event.setAdapter(adapter);
        type_of_event.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public CharSequence terminateToken(CharSequence charSequence) {
                if (cat.contains(charSequence.toString())) {
                    cat.remove(charSequence.toString());
                    catString = daysString.replace(charSequence.toString() + ", ", "");
                    return catString;
                } else {
                    cat.add(charSequence.toString());
                    catString += charSequence.toString() + ", ";
                    return catString;

                }
            }
        });

        final MultiAutoCompleteTextView off_day;
        off_day = findViewById(R.id.off_day);
        String[] item2 = {"No off days", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        off_day.setEnabled(false);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.city_drop_menu, item2);
        off_day.setAdapter(adapter2);
        off_day.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public int findTokenEnd(CharSequence charSequence, int i) {
                return 0;
            }

            @Override
            public CharSequence terminateToken(CharSequence charSequence) {
                if (days.contains(charSequence.toString())) {
                    days.remove(charSequence.toString());
                    daysString = daysString.replace(charSequence.toString() + ", ", "");
                    return daysString;
                } else {
                    days.add(charSequence.toString());
                    daysString += charSequence.toString() + ", ";
                    return daysString;
                }
            }
        });

        StartDate.setEnabled(false);
        EndDate.setEnabled(false);
        off_day.setEnabled(false);
        type_of_event.setEnabled(false);

        findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEmail(email.getText().toString())
                        && !isPhoneNumber(phoneNum.getText().toString())
                        && description.getText().toString().length() < 50) {
                    email.setError("Enter valid email address");
                    phoneNum.setError("Enter valid phone number");
                    description.setError("must be more than 50 character");
                    return;
                } else if (!isEmail(email.getText().toString())) {
                    email.setError("Enter valid email address");
                    return;
                } else if (!isPhoneNumber(phoneNum.getText().toString())) {
                    phoneNum.setError("Enter valid phone number");
                    return;
                } else if (description.getText().toString().length() < 50) {
                    description.setError("must be more than 50 character");
                    return;
                }

                Intent intent = new Intent(add_restaurant.this, SelectLocation.class);

                try {
                    intent.putExtra("owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                } catch (NullPointerException e) {
                    Toast.makeText(add_restaurant.this, "No current user", Toast.LENGTH_SHORT).show();
                }

                intent.putExtra("name", name.getText().toString());
                String categories;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    categories = type_of_event.getText().toString();
                    String[] strings = categories.split(", ");

                    intent.putExtra("categories", strings);
                }

                intent.putExtra("open", StartDate.getText().toString());
                intent.putExtra("close", EndDate.getText().toString());
                String offDays;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    offDays = off_day.getText().toString();
                    String[] strings = offDays.split(", ");
                    intent.putExtra("off", strings);
                }

                intent.putExtra("price", price.getText().toString());

                intent.putExtra("phone", phoneNum.getText().toString());
                intent.putExtra("email", email.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("image", coverPhoto.toString());

                intent.putExtra("type", "restaurant");
                intent.putExtra("key", serRef.getKey());
                startActivity(intent);

            }
        });

        radioButton.setChecked(true);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price.setText("");
                price.setEnabled(true);


            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ninjas rule
                price.setEnabled(false);
                price.setText("Free");
            }
        });

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        coverPhoto = uri;
                        imageView.setImageURI(uri);
                        if (coverPhoto == null
                                || name.getText().toString().equals("")
                                || StartDate.getText().toString().equals("")
                                || EndDate.getText().toString().equals("")
                                || phoneNum.getText().toString().equals("")
                                || email.getText().toString().equals("")
                                || description.getText().toString().equals("")
                                || off_day.getText().toString().equals("")
                                || type_of_event.getText().toString().equals("")) {
                            Next.setEnabled(false);
                        } else {
                            Next.setEnabled(true);
                        }
                    }
                };

                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(add_restaurant.this)
                        .setOnImageSelectedListener(listener)
                        .create();
                tedBottomPicker.show(getSupportFragmentManager());
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        off_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }

                if (name.getText().toString().equals("")) {
                    name.requestFocus();
                } else if (phoneNum.getText().toString().equals("")) {
                    phoneNum.requestFocus();
                } else if (email.getText().toString().equals("")) {
                    email.requestFocus();
                } else {
                    description.requestFocus();
                }
            }
        });

        StartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        EndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (coverPhoto == null
                        || name.getText().toString().equals("")
                        || StartDate.getText().toString().equals("")
                        || EndDate.getText().toString().equals("")
                        || phoneNum.getText().toString().equals("")
                        || email.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || off_day.getText().toString().equals("")
                        || type_of_event.getText().toString().equals("")
                        || price.getText().toString().equals("")) {
                    Next.setEnabled(false);
                } else {
                    Next.setEnabled(true);
                }
            }
        });


    }

    void SetStartDate() {

        Calendar calendar;

        calendar = Calendar.getInstance();
        StartHour = calendar.get(Calendar.HOUR_OF_DAY);
        StartMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;

        timePickerDialog = new TimePickerDialog(add_restaurant.this, new TimePickerDialog.OnTimeSetListener() {
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


                StartDate.setText("");
                StartDate.setText(StartDate.getText().toString() + hour + ":" + minute);

                if (name.getText().toString().equals("")) {
                    name.requestFocus();
                } else if (phoneNum.getText().toString().equals("")) {
                    phoneNum.requestFocus();
                } else if (email.getText().toString().equals("")) {
                    email.requestFocus();
                } else {
                    description.requestFocus();
                }
            }
        }, StartHour, StartMinute, true);

        timePickerDialog.show();


    }


    void SetEndDate() {
        Calendar calendar;

        calendar = Calendar.getInstance();
        EndHour = calendar.get(Calendar.HOUR_OF_DAY);
        EndMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;

        timePickerDialog = new TimePickerDialog(add_restaurant.this, new TimePickerDialog.OnTimeSetListener() {
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

                EndDate.setText("");
                EndDate.setText(EndDate.getText().toString() + hour + ":" + minute);

                if (name.getText().toString().equals("")) {
                    name.requestFocus();
                } else if (phoneNum.getText().toString().equals("")) {
                    phoneNum.requestFocus();
                } else if (email.getText().toString().equals("")) {
                    email.requestFocus();
                } else {
                    description.requestFocus();
                }
            }
        }, EndHour, EndMinute, true);

        timePickerDialog.show();
    }

    StorageReference storageReference;

    private void uploadFiles(Uri uri, final DatabaseReference serRef) {
        storageReference = FirebaseStorage.getInstance().getReference("services").child(serRef.getKey()).child("images");

        StorageReference reference = storageReference.child(System.currentTimeMillis() + "");
        reference.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_restaurant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(add_restaurant.this, "successful upload ", Toast.LENGTH_SHORT).show();
                serRef.child("cover photo").setValue(taskSnapshot.getUploadSessionUri().toString());
            }
        });

    }

    public boolean isPhoneNumber(String phone) {
        if (phone.length() == 10)
            return phone.charAt(0) == '0' && phone.charAt(1) == '7'
                    && (phone.charAt(2) == '7' || phone.charAt(2) == '8' || phone.charAt(2) == '9')
                    && phone.matches("[0-9]+");


        else {
            return false;
        }
    }

    public boolean isEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(add_restaurant.this, R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);
        builder.setTitle("Are you sure you want to cancel adding place?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(add_restaurant.this, services.class);
                startActivity(intent);
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
