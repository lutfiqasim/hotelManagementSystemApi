package bzu.edu.hotelManagmentAPI;

import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.*;
import bzu.edu.hotelManagmentAPI.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HotelManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApiApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializeDatabase(RoomStatusRepository roomStatusRepository,
                                                RoomClassRepository roomClassRepository,
                                                FloorRepository floorRepository,
                                                RoleRepository roleRepository,
                                                RoomRepository roomRepository) {
        return args -> {
            if (roomStatusRepository.count() == 0) {
                roomStatusRepository.saveAll(Arrays.asList(
                        new RoomStatus(RoomStatusEnum.Reserved.name()),
                        new RoomStatus(RoomStatusEnum.Available.name())
                ));
            }

            if (roomClassRepository.count() == 0) {
                roomClassRepository.saveAll(Arrays.asList(
                        new RoomClass("Standard", 2, 100.00f),
                        new RoomClass("Deluxe", 3, 150.00f),
                        new RoomClass("Suite", 4, 250.00f)
                ));
            }

            if (floorRepository.count() == 0) {
                floorRepository.saveAll(Arrays.asList(
                        new Floor(1),
                        new Floor(2),
                        new Floor(3)
                ));
            }
            if (roleRepository.count() == 0) {
                roleRepository.saveAll(Arrays.asList(
                        new Role(UserRole.ADMIN),
                        new Role(UserRole.CUSTOMER)
                ));
            }
            if (roomRepository.count() == 0) {
                List<Floor> floorList = floorRepository.findAll();
                List<RoomClass> classList = roomClassRepository.findAll();
                List<RoomStatus> roomStatuses = roomStatusRepository.findAll();
                roomRepository.saveAll(Arrays.asList(
                        new Room(floorList.get(0), classList.get(0), roomStatuses.get(1), "101"),
                        new Room(floorList.get(0), classList.get(1), roomStatuses.get(1), "102"),
                        new Room(floorList.get(0), classList.get(2), roomStatuses.get(1), "103"),
                        new Room(floorList.get(1), classList.get(0), roomStatuses.get(1), "201"),
                        new Room(floorList.get(1), classList.get(1), roomStatuses.get(1), "202"),
                        new Room(floorList.get(1), classList.get(2), roomStatuses.get(1), "203"),
                        new Room(floorList.get(2), classList.get(0), roomStatuses.get(1), "301"),
                        new Room(floorList.get(2), classList.get(1), roomStatuses.get(1), "302")
                ));
            }
        };
    }

}
