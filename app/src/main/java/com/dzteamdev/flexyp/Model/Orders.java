package com.dzteamdev.flexyp.Model;

public class Orders {
    private String productName, quantity, price, discount, type;

    public Orders() {
    }

    public Orders(String productName, String quantity, String price, String discount, String type) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
