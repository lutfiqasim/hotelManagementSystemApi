package bzu.edu.hotelManagmentAPI;

import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.*;
import bzu.edu.hotelManagmentAPI.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

@SpringBootApplication
public class HotelManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApiApplication.class, args);
    }

    @Bean
    @Transactional
    public ApplicationRunner initializeDatabase(RoomStatusRepository roomStatusRepository,
                                                RoomClassRepository roomClassRepository,
                                                FloorRepository floorRepository,
                                                RoleRepository roleRepository,
                                                RoomRepository roomRepository) {
        return args -> {
            if (roomStatusRepository.count() == 0) {
                roomStatusRepository.saveAll(Arrays.asList(
                        new RoomStatus(RoomStatusEnum.Reserved),
                        new RoomStatus(RoomStatusEnum.Available)
                ));
            }

            if (roomClassRepository.count() == 0) {
                roomClassRepository.saveAll(Arrays.asList(
                        new RoomClass(RoomClassEnum.DELUXE, 2, 100.00f),
                        new RoomClass(RoomClassEnum.STANDARD, 3, 150.00f),
                        new RoomClass(RoomClassEnum.SUITE, 4, 250.00f)
                ));
            }

            if (floorRepository.count() == 0) {
                floorRepository.saveAll(Arrays.asList(
                        new Floor(2),
                        new Floor(3),
                        new Floor(4)
                ));
            }

            if (roleRepository.count() == 0) {
                roleRepository.saveAll(Arrays.asList(
                        new Role(UserRole.ADMIN),
                        new Role(UserRole.CUSTOMER)
                ));
            }

        };
    }
}
