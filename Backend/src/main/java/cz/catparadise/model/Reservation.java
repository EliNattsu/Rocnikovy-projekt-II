package cz.catparadise.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.catparadise.validation.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "reservationId")
@ValidDateRange
@Entity
@Table(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @NotNull(message = "Datum přijezdu je povinný")
    @FutureOrPresent(message = "Datum nemůže být v minulosti")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "Datum odjezdu je povinný")
    @Future(message = "Datum odjezdu musí být v budoucnosti")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false, length = 40)
    private String status;

    @Column(name = "reference_number")
    private String referenceNumber;
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }


    // Vazba na uživatele – obrácená reference vůči User.getReservations()
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-reservations")
    private User user;

    // Vazba na kočky
    @ManyToMany
    @JoinTable(
            name = "Cat_Reservation",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "cat_id")
    )
    private Set<Cat> cats = new HashSet<>();

    //Vazba na pokoje
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    public Room getRoom(){return room;}
    public void setRoom(Room room) {this.room = room;}

    public Reservation() {}

    public Reservation(LocalDate startDate, LocalDate endDate, String status, User user) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
    }

    // --- Gettery a settery ---
    public Integer getReservationId() { return reservationId; }
    public void setReservationId(Integer reservationId) { this.reservationId = reservationId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Cat> getCats() { return cats; }
    public void setCats(Set<Cat> cats) { this.cats = cats; }
}