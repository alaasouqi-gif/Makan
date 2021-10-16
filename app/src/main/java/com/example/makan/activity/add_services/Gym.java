package com.example.makan.activity.add_services;

public class Gym {

    double lat;
    double lng;
    String city;
    String name;
    String open;
    String close;
    String phone;
    String email;
    String des;
    String type;

    public Gym(double lat, double lng, String city, String name, String open, String close, String phone, String email, String des) {
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.name = name;
        this.open = open;
        this.close = close;

        this.phone = phone;
        this.email = email;
        this.des = des;
        this.type = "restaurant";
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }


    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDes() {
        return des;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setClose(String close) {
        this.close = close;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDes(String des) {
        this.des = des;
    }


}
