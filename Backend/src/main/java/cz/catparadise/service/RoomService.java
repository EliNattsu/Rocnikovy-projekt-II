package cz.catparadise.service;

import cz.catparadise.model.Room;
import cz.catparadise.model.RoomType;
import cz.catparadise.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;
    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public Optional<Room> getRoomById(Integer id) {
        return roomRepository.findById(id);
    }
    public void deleteRoom(Integer id) {
        roomRepository.deleteById(id);
    }
    public List<Room> getRoomsByType(RoomType type){
        return roomRepository.findByType(type);
    }
    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date must be before end date");
        }
        return roomRepository.findAvailableRooms(startDate, endDate);
    }
}
