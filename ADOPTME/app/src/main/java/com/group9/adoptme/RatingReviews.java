package com.group9.adoptme;

public class RatingReviews {
    private int rateID, userID, petID;
    private String petRate;
    private String rateReview, rateDate, fullname, userphoto;

    public RatingReviews(int rateID, int userID, int petID, String petRate, String rateReview, String rateDate, String fullname, String userphoto) {
        this.rateID = rateID;
        this.userID = userID;
        this.petID = petID;
        this.petRate = petRate;
        this.rateReview = rateReview;
        this.rateDate = rateDate;
        this.fullname = fullname;
        this.userphoto = userphoto;
    }

    public int getRateID() {
        return rateID;
    }

    public int getUserID() {
        return userID;
    }

    public int getPetID() {
        return petID;
    }

    public String getPetRate() {
        return petRate;
    }

    public String getRateReview() {
        return rateReview;
    }

    public String getRateDate() {
        return rateDate;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUserphoto() {
        return userphoto;
    }
}

