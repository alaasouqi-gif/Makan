package com.example.makan.Data;

import java.io.Serializable;

public class reservation implements Serializable {
    public String name;
    public String image;
    public String serviceId;
    public String service_type;

    public String date;
    public String reservation_id;
    public String payment_date;
    public String amount;
    public String num_tic;
    public String open;
    public String close;

    public reservation(String name, String image, String serviceId, String service_type, String date, String reservation_id, String payment_date, String amount, String tic) {
        this.name = name;
        this.image = image;
        this.serviceId = serviceId;
        this.service_type = service_type;
        this.date = date;
        this.reservation_id = reservation_id;
        this.payment_date = payment_date;
        this.amount = amount;
        this.num_tic = tic;
    }

    public reservation(String name, String image, String serviceId, String service_type, String date, String reservation_id, String payment_date, String amount, String open, String close) {
        this.name = name;
        this.image = image;
        this.serviceId = serviceId;
        this.service_type = service_type;
        this.date = date;
        this.reservation_id = reservation_id;
        this.payment_date = payment_date;
        this.amount = amount;
        this.open = open;
        this.close = close;

    }

}
