package com.example.makan.activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class usersData {

    //Add Users To Database With Google
    public void addUserToDB_GOOGLE(String userId, String email, String name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId);

        String[] arrOfStr = name.split(" ");

        myRef.child("Email").setValue(email);
        myRef.child("First Name").setValue(arrOfStr[0]);
        myRef.child("Last Name").setValue(arrOfStr[1]);

    }

    //Add Users To Database With Email & Password
    public void addUserToDB_EMAIL(String userId, String email, String fname, String lname, String phone, String city, String BD) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId);

        myRef.child("Email").setValue(email);
        myRef.child("First Name").setValue(fname);
        myRef.child("Last Name").setValue(lname);
        myRef.child("Phone Number").setValue(phone);
        myRef.child("City").setValue(city);
        myRef.child("Birth Date").setValue(BD);
    }


    //Change Users First Name
    public void changeUsersFname(String userId, String fName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId).child("First Name");
        myRef.setValue(fName);
    }

    //Change Users Last Name
    public void changeUsersLname(String userId, String lName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId).child("Last Name");
        myRef.setValue(lName);
    }

    //Change Users Email
    public void changeUsersEmail(String userId, String email) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId).child("Email");
        myRef.setValue(email);
    }

    //Change Users Phone Number
    public void changeUsersPhone(String userId, String phone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId).child("Phone Number");
        myRef.setValue(phone);
    }

    //Change Users City
    public void changeUsersCity(String userId, String city) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(userId).child("City");
        myRef.setValue(city);
    }


    public void addUserToDB_EMAIL(String uid, String phone, String city, String birthDate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(uid);
        myRef.child("Phone Number").setValue(phone);
        myRef.child("City").setValue(city);
        myRef.child("Birth Date").setValue(birthDate);

    }
}
