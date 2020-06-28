package com.dzteamdev.flexyp.Model;

import java.io.Serializable;

public class Offers implements Serializable {
    private String name, price, img;

    public Offers() {
    }

    public Offers(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Offers(String name, String price, String img) {
        this.name = name;
        this.price = price;
        this.img = img;
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
