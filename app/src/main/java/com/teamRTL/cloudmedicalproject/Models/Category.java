package com.teamRTL.cloudmedicalproject.Models;

public class Category {
    private String icon;
    private String title;

    public Category() {
        // Required empty constructor for Firebase Firestore deserialization
    }
    public Category(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
