package com.example.makan.Data;


public class KindOfInterest {

    public int Image;
    public String Name;
    public int select;

    public KindOfInterest(int image, String name) {
        Image = image;
        Name = name;
        select = 0;
    }

    public KindOfInterest(int image, String name, int s) {
        Image = image;
        Name = name;
        select = s;
    }

}
