package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.enums.RoomClassEnum;
import bzu.edu.hotelManagmentAPI.model.RoomClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomClassRepository extends JpaRepository<RoomClass, Long> {

    @Query("SELECT r FROM RoomClass r WHERE r.className = :name")
    // Without query, className type will have to be RoomClassEnum not String
    RoomClass findByClassName(RoomClassEnum name);
}
