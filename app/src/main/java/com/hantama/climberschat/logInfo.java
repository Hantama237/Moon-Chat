package com.hantama.climberschat;

public class logInfo {
    private String email;
    private String password;

    public logInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public logInfo() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
