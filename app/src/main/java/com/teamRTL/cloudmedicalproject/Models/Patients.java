package com.teamRTL.cloudmedicalproject.Models;

public class Patients {

    private String id,name,date,address,email,phone,image;


    public Patients() {
    }
    public Patients(String name,String image) {
        this.name = name;
        this.image = image;
    }

    public Patients(String id,String name, String date, String address, String email, String phone,String image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.image = image;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
