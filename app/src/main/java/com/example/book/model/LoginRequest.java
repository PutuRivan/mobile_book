package com.example.book.model;

public class LoginRequest {
    private String user_email;
    private String user_password;

    public LoginRequest(String user_email, String user_password) {
        this.user_email = user_email;
        this.user_password = user_password;
    }
}
