package cz.catparadise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roomId;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Room name cannot be blank")
    private String name;

    @Convert(converter = RoomTypeConverter.class)
    @Column(name="type", nullable=false)
    @NotNull(message = "Typ pokoje je povinný")
    private RoomType type;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();

    public Room() {}
    public Room(String name, RoomType type){
        this.name = name;
        this.type = type;
    }

    public Integer getRoomId() {return roomId;}
    public void setRoomId(Integer roomId){this.roomId = roomId;}
    public String getName() {return name;}
    public void setName(String name){this.name = name;}
    public RoomType getType() {return type;}
    public void setType(RoomType type){this.type = type;}
    public Set<Reservation> getReservations() {return reservations;}
    public void setReservations(Set<Reservation> reservations){this.reservations = reservations;}
}
