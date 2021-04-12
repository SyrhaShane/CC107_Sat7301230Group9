package com.group9.adoptme;

public class UsersTimeline {
    private String userphoto, fullname, petDate, petName, petPhoto, petID, petCategory, petProcedure;

    public UsersTimeline(String userphoto, String fullname, String petDate, String petName, String petPhoto, String petID, String petCategory, String petProcedure) {
        this.userphoto = userphoto;
        this.fullname = fullname;
        this.petDate = petDate;
        this.petName = petName;
        this.petPhoto = petPhoto;
        this.petCategory = petCategory;
        this.petID = petID;
        this.petProcedure = petProcedure;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPetDate() {
        return petDate;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetPhoto() {
        return petPhoto;
    }

    public String getPetID() {
        return petID;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public String getPetProcedure() {
        return petProcedure;
    }
}

