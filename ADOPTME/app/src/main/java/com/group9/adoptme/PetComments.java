package com.group9.adoptme;

public class PetComments {
    private String commentID, comments, commentDate,petID,  userphoto, fullname;

    public PetComments(String commentID, String comments, String commentDate, String petID, String userphoto, String fullname) {
        this.commentID = commentID;
        this.comments = comments;
        this.commentDate = commentDate;
        this.petID = petID;
        this.userphoto = userphoto;
        this.fullname = fullname;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getComments() {
        return comments;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getPetID() {
        return petID;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getFullname() {
        return fullname;
    }
}
