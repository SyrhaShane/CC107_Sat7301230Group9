package com.group9.adoptme;

public class UsersFavorite {
    private String userphoto, fullname, petDate, petName, petPhoto, favValue, petID;

    public UsersFavorite(String userphoto, String fullname, String petDate, String petName, String petPhoto, String favValue, String petID) {
        this.userphoto = userphoto;
        this.fullname = fullname;
        this.petDate = petDate;
        this.petName = petName;
        this.petPhoto = petPhoto;
        this.favValue = favValue;
        this.petID = petID;

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

    public String getFavValue() {
        return favValue;
    }

    public String getPetID() {
        return petID;
    }
}

