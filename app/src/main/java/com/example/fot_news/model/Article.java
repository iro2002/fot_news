package com.example.fot_news.model;

public class Article {
    private String title;
    private String description;
    private String time; // The time in a format like "9h ago", "2d ago", etc.
    private String image; // This can be an image URL or a drawable resource name

    // Default constructor for Firebase
    public Article() {}

    // Constructor with parameters
    public Article(String title, String description, String time, String image) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.image = image;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    // The 'getTimeAgo' method for displaying time ago, you can customize this as per your needs.
    public String getTimeAgo() {
        return time; // Assuming 'time' already contains the formatted "time ago" string.
    }
}
