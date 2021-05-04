package com.example.communityhubproject;

public class model {
    String title;
    String description ;
    String price;
    String imageURL;
    String userId;

    public model() {
    }


    public model(String title, String description, String price, String imageURL, String userId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
