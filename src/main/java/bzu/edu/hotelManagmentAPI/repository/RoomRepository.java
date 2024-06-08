package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.floor.floor_number = :floor")
    Page<Room> findByFloor(Integer floorNo, Pageable pageable);

    Page<Room> findAll(Pageable pageable);

    @Query("SELECT r FROM Room r WHERE (:floor is null or r.floor.floor_number = :floor) AND (:date is null or r.date = :date) AND (:size is null or r.size = :size)")
    Page<Room> findAll(Integer floorNo, LocalDate date, Integer size); //API V2 only

    List<Room> findByDate(LocalDate date);

    @Query("SELECT r FROM Room r WHERE r.size = :size")
    List<Room> findAllBySize(Integer size, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.available = true")
    List<Room> findAvailableRooms();

}
