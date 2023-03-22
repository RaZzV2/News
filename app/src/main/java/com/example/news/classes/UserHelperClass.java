package com.example.news.classes;

public class UserHelperClass {
    String email, username, password;
    boolean isConfirmedPhone;

    public UserHelperClass(String email, String username, boolean isConfirmedPhone) {
        this.email = email;
        this.username = username;
        this.isConfirmedPhone = isConfirmedPhone;
    }

    public boolean isConfirmedPhone() {
        return isConfirmedPhone;
    }

    public void setConfirmedPhone(boolean confirmedPhone) {
        isConfirmedPhone = confirmedPhone;
    }

    public UserHelperClass() {
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
