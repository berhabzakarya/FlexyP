package com.dzteamdev.flexyp.Model;

public class Users {

    private String mobileNumber, fullName, password, codePin, verifyNumber;
    private String email;
    private String img;
    private String wallet;
    private boolean inStuff;

    public Users() {
    }

    public Users(String mobileNumber, String fullName, String password, String codePin, String verifyNumber) {
        this.mobileNumber = mobileNumber;
        this.fullName = fullName;
        this.password = password;
        this.codePin = codePin;
        this.verifyNumber = verifyNumber;
        this.wallet = "0";
        this.inStuff = false;
    }

    public boolean isInStuff() {
        return inStuff;
    }

    public void setInStuff(boolean inStuff) {
        this.inStuff = inStuff;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodePin() {
        return codePin;
    }

    public void setCodePin(String codePin) {
        this.codePin = codePin;
    }

    public String getVerifyNumber() {
        return verifyNumber;
    }

    public void setVerifyNumber(String verifyNumber) {
        this.verifyNumber = verifyNumber;
    }
}
