package cz.catparadise.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name="first_name", nullable=false, length = 50)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Column(name="last_name", nullable=false, length = 50)
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Column(name="email", nullable=false, unique=true, length=100)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name="password_hash", nullable=true)
    private String passwordHash;

    @Column(name="phone_number", nullable=false, unique=true, length=20)
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[0-9]{9,13}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Column(name="registration_date", nullable=false)
    private LocalDateTime registrationDate;

    @Column(name="role", nullable=false)
    private String role = "USER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-cats")
    private Set<Cat> cats = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user-reservations")
    private Set<Reservation> reservations = new HashSet<>();

    public User() {}

    public User(String firstName, String lastName, String email, String passwordHash, String phoneNumber, LocalDateTime registrationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    // --- Gettery a settery ---
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public Set<Cat> getCats() { return cats; }
    public void setCats(Set<Cat> cats) { this.cats = cats; }

    public Set<Reservation> getReservations() { return reservations; }
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}