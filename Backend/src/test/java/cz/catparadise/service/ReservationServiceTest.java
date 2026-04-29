package cz.catparadise.service;

import cz.catparadise.model.Reservation;
import cz.catparadise.model.Room;
import cz.catparadise.model.RoomType;
import cz.catparadise.model.User;
import cz.catparadise.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);

        room = new Room();
        room.setRoomId(1);
        room.setName("Mini pokoj");
        room.setType(RoomType.Pokoj_pro_kočku);

        reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(5));
        reservation.setUser(user);
        reservation.setRoom(room);
    }
    //Test uspesneho vytvoreni rezervace kdy je pokoj volny
    @Test
    void saveReservation_shouldSaveWhenIsRoomAvalible(){
        when(reservationRepository.findOverlappingReservations(
                any(), any(), any())).thenReturn(Collections.emptyList());
        when(reservationRepository.save(any())).thenReturn(reservation);

        Reservation saved = reservationService.saveReservation(reservation);

        assertNotNull(saved);
        assertEquals("PENDING", saved.getStatus());
        assertNotNull(saved.getReferenceNumber());
        verify(reservationRepository, times(1)).save(any());
    }

    //Test kdy je jiz pokoj obsazeny
    @Test
    void saveReservation_shouldThrowWhenIsRoomNotAvalible(){
        when(reservationRepository.findOverlappingReservations(
                any(), any(), any())).thenReturn(List.of(reservation));

        assertThrows(IllegalArgumentException.class, () -> reservationService.saveReservation(reservation));
        verify(reservationRepository, never()).save(any());
    }

    //Test nalezeni rezervace podle ID
    @Test
    void getReservationById_shouldReturnReservation(){
        when(reservationRepository.findById((1))).thenReturn(Optional.of(reservation));

        Optional<Reservation> found = reservationService.getReservationById(1);

        assertTrue(found.isPresent());
        assertEquals(reservation, found.get());
    }

    //Test zmeny statusu rezervace
    @Test
    void updateReservationStatus_shouldUpdateStatus(){
        reservation.setReservationId(1);
        when(reservationRepository.findById((1))).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);

        Reservation updated = reservationService.updateReservationStatus(1, "CONFIRMED");

        assertEquals("CONFIRMED", updated.getStatus());
        verify(reservationRepository, times(1)).save(any());
    }

    //Test mazani rezervace
    @Test
    void deleteReservation_shouldCallRepository(){
        reservationService.deleteReservation(1);

        verify(reservationRepository, times(1)).deleteById(1);
    }
}
