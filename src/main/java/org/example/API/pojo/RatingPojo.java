package org.example.API.pojo;

import org.example.API.model.Rating;

public class RatingPojo {

    private Rating rating;
    private String userReference;

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }

    public String getUserReference() {
        return userReference;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
