package com.example.makan.Data;

public class notification {
    public String title;
    public String description;
    public String image;
    public String service_id;
    public String SType;
    public String id;
    public String open;

    public notification(String title, String description, String image, String service_id, String SType, String id, String open) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.service_id = service_id;
        this.SType = SType;
        this.id = id;
        this.open = open;
    }

}
