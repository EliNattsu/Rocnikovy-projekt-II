package cz.catparadise.controller;

import cz.catparadise.model.Room;
import cz.catparadise.model.RoomType;
import cz.catparadise.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    //Výpis všech pokojů
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    //Detaily pokoje
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Integer id){
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Vyhledání volných pokojů
    @GetMapping("/available")
    public List<Room> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ){
        return roomService.getAvailableRooms(startDate, endDate);
    }

    //Vyhledání pokojů podle typu
    @GetMapping("/by-type")
    public List<Room> getRoomsByType(@RequestParam RoomType type){
        return roomService.getRoomsByType(type);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room){
        Room saved = roomService.saveRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    //ÚPRAVA POKOJU
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@PathVariable Integer id, @Valid @RequestBody Room updated){
        return roomService.getRoomById(id)
                .map(room -> {
                    room.setName(updated.getName());
                    room.setType(updated.getType());
                    return ResponseEntity.ok(roomService.saveRoom(room));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //Smazání pokoje
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id){
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
