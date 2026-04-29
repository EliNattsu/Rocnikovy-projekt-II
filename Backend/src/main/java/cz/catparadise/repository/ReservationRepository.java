package cz.catparadise.repository;

import cz.catparadise.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByStatus(String status);
    List<Reservation> findByUserUserId(Integer userId);
    //Najde všechny rezervace, které se překrývají s daným termínem
    @Query("SELECT r from Reservation r WHERE r.room.roomId = :roomId " +
            "and r.startDate < :endDate and r.endDate > :startDate")
    List<Reservation> findOverlappingReservations(@Param("roomId") Integer roomId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
    Page<Reservation> findByUserUserId(Integer userId, Pageable pageable);
}