package cz.catparadise.repository;

import cz.catparadise.model.Room;
import cz.catparadise.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDate;

public interface RoomRepository extends JpaRepository<Room, Integer>{
    List<Room> findByType(RoomType type);
    //Najde všechny volné pokoje
    @Query("SELECT r FROM Room r WHERE r.roomId NOT IN (" +
            "select res.room.roomId FROM Reservation res " +
     "Where res.startDate < :endDate and res.endDate > :startDate)")
    List<Room> findAvailableRooms(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);
}
