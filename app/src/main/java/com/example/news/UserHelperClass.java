package com.example.news;

public class UserHelperClass {
    String email, username, password, phone_number;

    public UserHelperClass(String email, String username, String phone_number) {
        this.email = email;
        this.username = username;
        this.phone_number=phone_number;
    }

    public UserHelperClass() {
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
