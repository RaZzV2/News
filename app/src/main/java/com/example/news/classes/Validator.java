package com.example.news.classes;

import android.util.Patterns;

public class Validator {

    public boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidUsername(String username){
        String pattern = "^[a-zA-Z][a-zA-Z\\d_.]{3,19}$";
        return username.matches(pattern);
    }

    public boolean isValidPassword(String password) {
        String pattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\\\~`! @#$%^&*()_\\-+={\\[}\\]\\|\\:;\\\"'<,>.?/])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }

    public boolean isValidPhoneNumber(String phoneNumber){
        String pattern = "^\\+40[1-9]\\d{8}$";
        return phoneNumber.matches(pattern);
    }
}
