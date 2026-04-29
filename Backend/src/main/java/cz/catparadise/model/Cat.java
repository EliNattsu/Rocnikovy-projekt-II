package cz.catparadise.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "catId"
)
@Entity
@Table(name = "Cats")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catId;

    @Column(name="cat_name", nullable=false, length = 50)
    @NotBlank(message = "Cat name cannot be blank")
    private String catName;

    @Column(name="age", nullable=false)
    @NotNull(message = "Age cannot be blank")
    @Min(value = 0, message = "Age must be at least 0 year old")
    @Max(value = 30, message = "Age cannot be more than 30 years old")
    private Integer age;

    @Column(name="notes", columnDefinition = "TEXT")
    private String notes;

    // Vazba na uživatele
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable=false)
    @JsonBackReference(value = "user-cats")
    private User user;

    // Vazba na rezervace
    @ManyToMany(mappedBy = "cats", fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();

    public Cat() {}

    public Cat(String catName, Integer age, String notes, User user) {
        this.catName = catName;
        this.age = age;
        this.notes = notes;
        this.user = user;
    }

    public Integer getCatId() { return catId; }
    public void setCatId(Integer catId) { this.catId = catId; }

    public String getCatName() { return catName; }
    public void setCatName(String catName) { this.catName = catName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Reservation> getReservations() { return reservations; }
    public void setReservations(Set<Reservation> reservations) { this.reservations = reservations; }
}
