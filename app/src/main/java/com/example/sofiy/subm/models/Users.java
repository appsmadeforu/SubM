package com.example.sofiy.subm.models;

public class Users {
    private String Username;
    private String Contact;
    private String Email;


    public Users(){

    }

    public Users(String Username, String Contact, String Email){
        this.Username = Username;
        this.Contact = Contact;
        this.Email = Email;

    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

