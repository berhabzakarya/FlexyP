package com.dzteamdev.flexyp.Model;

import java.io.Serializable;

public class Offers implements Serializable {
    private String name, price, img;
    private String description;
    private String id;

    public Offers() {
    }

    public Offers(String name, String price, String img, String description, String id) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.description = description;
        this.id = id;
    }

    public Offers(String name, String price, String img, String description) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
