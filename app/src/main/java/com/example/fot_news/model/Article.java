package com.example.fot_news.model; // Ensure package name is correct

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
    private String title;
    private String description;
    private String image;
    private String time;

    public Article() {
        // Default constructor required for calls to DataSnapshot.getValue(Article.class)
    }

    public Article(String title, String description, String image, String time) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.time = time;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public String getTime() { return time; }

    // Setters (if needed, e.g., for Firebase, though it often uses getters and constructors)
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setImage(String image) { this.image = image; }
    public void setTime(String time) { this.time = time; }

    // --- Parcelable implementation ---

    protected Article(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
        time = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(time);
    }
}