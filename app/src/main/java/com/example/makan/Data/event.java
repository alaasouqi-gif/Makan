package com.example.makan.Data;

import android.net.Uri;


public class event {
    public String title;
    public String place;
    public String des;
    public String pop;
    public String id;
    public String type;
    public Uri image;

    public event(String title, String place, String des, String id, String type, Uri image) {
        this.title = title;
        this.place = place;
        this.image = image;
        this.des = des;
        this.id = id;
        this.type = type;

    }

    public event(String title, String Pop, String id, String type, Uri image) {
        this.title = title;
        this.image = image;
        this.id = id;
        pop = Pop;
        this.type = type;

    }


}
