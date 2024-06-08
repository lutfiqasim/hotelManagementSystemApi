package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long> {
    Optional<RoomStatus> findByStatusName(RoomStatusEnum statusName);
}
