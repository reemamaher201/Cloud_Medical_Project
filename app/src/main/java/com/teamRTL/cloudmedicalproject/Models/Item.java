package com.teamRTL.cloudmedicalproject.Models;

public class Item {
    private String text;
    private int imageResource;

    public Item(String text, int imageResource) {
        this.text = text;
        this.imageResource = imageResource;
    }

    // Getters and setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
