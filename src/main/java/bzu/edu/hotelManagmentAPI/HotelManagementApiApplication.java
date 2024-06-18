package bzu.edu.hotelManagmentAPI;

import bzu.edu.hotelManagmentAPI.enums.RoomClassEnum;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.*;
import bzu.edu.hotelManagmentAPI.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApiApplication.class, args);
    }
}
