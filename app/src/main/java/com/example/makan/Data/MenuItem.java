package com.example.makan.Data;

import android.net.Uri;

public class MenuItem {

    public String Name;
    public String category;
    public String Price;
    public Uri image;


    public MenuItem(String name, String price, Uri image, String cat) {
        Name = name;
        Price = price;
        category = cat;
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public String getPrice() {
        return Price;
    }

    public Uri getImage() {
        return image;
    }
}
