package com.teamRTL.cloudmedicalproject.Models;

public class OnBoadringItem {
String title;
String description;
int screenImage;

public OnBoadringItem(String title,String description,int screenImage){
    this.title = title;
    this.description = description;
    this.screenImage = screenImage;
}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScreenImage(int screenImage) {
        this.screenImage = screenImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getScreenImage() {
        return screenImage;
    }

}

