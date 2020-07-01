package com.dzteamdev.flexyp.Model;

import java.util.List;

public class Requests {
    private String fullName, totalAmount, date, status;
    private List<Orders> list;

    public Requests() {
    }

    public Requests(String fullName, String totalAmount, String date, String status, List<Orders> list) {
        this.fullName = fullName;
        this.totalAmount = totalAmount;
        this.date = date;
        this.status = status;
    }

    public List<Orders> getList() {
        return list;
    }

    public void setList(List<Orders> list) {
        this.list = list;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
