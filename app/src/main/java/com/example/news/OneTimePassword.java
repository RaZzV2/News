package com.example.news;

import java.util.Date;

public class OneTimePassword {
    String otpCode;
    String phoneNumber;
    private Date time;

    public OneTimePassword() {
    }
    public OneTimePassword(String otpCode, String phoneNumber, Date time) {
        this.otpCode = otpCode;
        this.phoneNumber = phoneNumber;
        this.time = time;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void saveToFirebase(){
        AuthManager authManager = new AuthManager();
        String generateOtpUniqueId = authManager.getOTPReference().push().getKey();
        assert generateOtpUniqueId != null;
        authManager.getOTPReference().child(generateOtpUniqueId).setValue(this);
    }
}
