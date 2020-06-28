package com.dzteamdev.flexyp.Model;

import java.io.Serializable;

public class Offers implements Serializable {
    private String name, price;

    public Offers() {
    }

    public Offers(String name, String price) {
        this.name = name;
        this.price = price;
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

}
