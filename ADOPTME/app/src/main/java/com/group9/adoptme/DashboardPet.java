package com.group9.adoptme;

public class DashboardPet {
    private int petID, userID;//, likeValue, favValue;
    private String petName, petCategory, petProcedure, petDate, petPhoto, userphoto, fullname;


    public DashboardPet(int mealID, String mealName, String mealCategory, String mealPhoto, String mealProcedure, String mealDate, int userID, String userphoto, String fullname)//, int likeValue, int favValue)
    {
        this.petID = mealID;
        this.userID = userID;
        this.petName = mealName;
        this.petCategory = mealCategory;
        this.petProcedure = mealProcedure;
        this.petDate = mealDate;
        this.petPhoto = mealPhoto;
        this.userphoto = userphoto;
        this.fullname= fullname;
        // this.likeValue = likeValue;
        //this.favValue = favValue;
    }

    public int getPetID() {
        return petID;
    }

    public int getUserID() {
        return userID;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public String getPetProcedure() {
        return petProcedure;
    }

    public String getPetDate() {
        return petDate;
    }

    public String getPetPhoto() {
        return petPhoto;
    }

    public String getuserPhoto() {
        return userphoto;
    }

    public String getFullname() {
        return fullname;
    }
/*
    public int getLikeValue() {
        return likeValue;
    }

    public int getFavValue() {
        return favValue;
    }

 */
}
