package com.example.demo.dto;

public class AuthDto {
    private String username;
    private String password;

    // 기본 생성자
    public AuthDto() {}

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}