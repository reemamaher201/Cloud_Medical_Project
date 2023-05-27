package com.teamRTL.cloudmedicalproject.Models;


public class Users {

    private String id ;
    private String email;
    private String name;
    private String image;
    private String status;
    private String search;

    public Users(String id, String name, String image, String status ,String search) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.search = search;
    }
    public Users(String id, String name, String image,String email) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;

    }

    public Users(){

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
