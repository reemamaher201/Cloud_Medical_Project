package com.teamRTL.cloudmedicalproject.Models;

import java.io.Serializable;

public class Category implements Serializable {
    String id;
    private String icon;
    private String title;

    public Category() {
        // Required empty constructor for Firebase Firestore deserialization
    }
    public Category(String icon, String title,String id) {
        this.icon = icon;
        this.title = title;
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
