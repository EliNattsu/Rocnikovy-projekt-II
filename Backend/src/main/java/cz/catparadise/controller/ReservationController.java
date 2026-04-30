package cz.catparadise.controller;

import cz.catparadise.model.Reservation;
import cz.catparadise.service.ReservationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Vytvoření rezervace
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) throws IllegalAccessException {
        Reservation saved = reservationService.saveReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/by-user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable Integer userId) {
        return reservationService.getReservationsByUser(userId);
    }

    // Výpis všech rezervací
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // Detaily rezervace (lepší přes ResponseEntity)
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Integer id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Rezervace podle statusu
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reservation> getReservationsByStatus(@PathVariable String status) {
        return reservationService.getReservationsByStatus(status);
    }

    // Úprava statusu rezervace
    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable Integer id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(reservationService.updateReservationStatus(id, status));
    }

    // Úprava celé rezervace (např. datumů, stavu atd.)
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Integer id,
            @RequestBody Reservation updated
    ) {
        return reservationService.getReservationById(id).map(reservation -> {
            reservation.setStartDate(updated.getStartDate());
            reservation.setEndDate(updated.getEndDate());
            return ResponseEntity.ok(reservationService.saveReservation(reservation));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Smazání rezervace
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user/{userId}/paged")
    public Page<Reservation> getReservationByUserPaged(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
                return reservationService.getReservationsByUserPaged(userId, PageRequest.of(page, size));
    }

}