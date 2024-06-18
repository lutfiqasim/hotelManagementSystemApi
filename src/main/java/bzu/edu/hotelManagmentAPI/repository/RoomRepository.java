package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.floor.floorNumber = :floor")
    Page<Room> findByFloorNumber(@Param("floor") Integer floorNo, Pageable pageable);

    Page<Room> findAll(Pageable pageable);

    //    @Query("SELECT r FROM Room r WHERE (:floor is null or r.floor.floorNumber = :floor) AND (:date is null or r.date = :date) AND (:size is null or r.size = :size)")
//    Page<Room> findAll(@Param("floor") Integer floorNo, @Param("date") LocalDate date, @Param("size") Integer size, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE (:floorNo IS NULL OR r.floor.floorNumber = :floorNo)")
    Page<Room> findAllPageable(@Param("floorNo") Integer floorNo, Pageable pageable);


//    List<Room> findByDate(LocalDate date);

//    @Query("SELECT r FROM Room r WHERE r.size = :size")
//    List<Room> findAllBySize(@Param("size") Integer size, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.status.statusName = 'Available'")
    List<Room> findAvailableRooms();
}