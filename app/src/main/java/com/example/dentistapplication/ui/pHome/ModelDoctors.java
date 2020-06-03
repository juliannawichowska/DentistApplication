package com.example.dentistapplication.ui.pHome;

public class ModelDoctors {

    String name, surname, address, imageURL, search, email, userUid;

    public ModelDoctors() {
    }

    public ModelDoctors(String name, String surname, String address, String imageURL, String search, String email) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.imageURL = imageURL;
        this.search = search;
        this.email = email;
        this.userUid = userUid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
