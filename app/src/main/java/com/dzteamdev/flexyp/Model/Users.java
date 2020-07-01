package com.dzteamdev.flexyp.Model;

public class Users {

    private boolean verifyNumber;
    private String mobileNumber, fullName, password, codePin, email, img, wallet;
    private boolean inStuff;

    public Users() {
    }

    public Users(String mobileNumber, String fullName, String password, String codePin, String email, String img, String wallet, boolean verifyNumber, boolean inStuff) {
        this.mobileNumber = mobileNumber;
        this.fullName = fullName;
        this.password = password;
        this.codePin = codePin;
        this.email = email;
        this.img = img;
        this.wallet = wallet;
        this.verifyNumber = verifyNumber;
        this.inStuff = inStuff;
    }


    public Users(String mobileNumber, String fullName, String password, String codePin, String email, String img, boolean inStuff) {
        this.mobileNumber = mobileNumber;
        this.fullName = fullName;
        this.password = password;
        this.codePin = codePin;
        this.email = email;
        this.img = img;
        this.inStuff = inStuff;
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

    public boolean getVerifyNumber() {
        return verifyNumber;
    }

    public void setVerifyNumber(boolean verifyNumber) {
        this.verifyNumber = verifyNumber;
    }
}
