package cz.catparadise.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String passwordHash;

    public LoginRequest() {}

    public LoginRequest(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String password) { this.passwordHash = password; }
}