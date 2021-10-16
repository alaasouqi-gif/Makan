package com.example.makan.activity.add_services;


public class Restaurant {

    double lat;
    double lng;
    String city;
    String name;
    String open;
    String close;
    String max;
    String tableNum;
    String price;
    String cancel;
    String phone_number;
    String email;
    String des;
    String type;

    public Restaurant(double lat, double lng, String city, String name, String open, String close, String max, String tableNum, String price, String cancel, String phone_number, String email, String des) {
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.name = name;
        this.open = open;
        this.close = close;
        this.max = max;
        this.tableNum = tableNum;
        this.price = price;
        this.cancel = cancel;
        this.phone_number = phone_number;
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

    public String getMax() {
        return max;
    }

    public String getTableNum() {
        return tableNum;
    }

    public String getPrice() {
        return price;
    }

    public String getCancel() {
        return cancel;
    }

    public String getPhone() {
        return phone_number;
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

    public void setMax(String max) {
        this.max = max;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public void setPhone(String phone) {
        this.phone_number = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDes(String des) {
        this.des = des;
    }


}

