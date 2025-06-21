package com.example.book.model;

public class LoginResponse {
    private String message;
    private Data data;
    private String token;

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public class Data {
        private String id;
        private String username;
        private String email;

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }
}
