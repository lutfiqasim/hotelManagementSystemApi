package bzu.edu.hotelManagmentAPI.configuration;

import bzu.edu.hotelManagmentAPI.enums.PaymentMethod;
import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import bzu.edu.hotelManagmentAPI.enums.RoomClassEnum;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.*;
import bzu.edu.hotelManagmentAPI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SampleDataLoader {

    private final FeatureRepository featureRepository;
    private final FloorRepository floorRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationRoomRepository reservationRoomRepository;
    private final RoleRepository roleRepository;
    private final RoomRepository roomRepository;
    private final RoomClassRepository roomClassRepository;
    private final RoomClassFeatureRepository roomClassFeatureRepository;
    private final RoomStatusRepository roomStatusRepository;
    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SampleDataLoader(FeatureRepository featureRepository, FloorRepository floorRepository, ReservationRepository reservationRepository, ReservationRoomRepository reservationRoomRepository, RoleRepository roleRepository, RoomRepository roomRepository, RoomClassRepository roomClassRepository, RoomClassFeatureRepository roomClassFeatureRepository, RoomStatusRepository roomStatusRepository, UserRepository userRepository, PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.featureRepository = featureRepository;
        this.floorRepository = floorRepository;
        this.reservationRepository = reservationRepository;
        this.reservationRoomRepository = reservationRoomRepository;
        this.roleRepository = roleRepository;
        this.roomRepository = roomRepository;
        this.roomClassRepository = roomClassRepository;
        this.roomClassFeatureRepository = roomClassFeatureRepository;
        this.roomStatusRepository = roomStatusRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void loadData() {
        // Check if data already exists
        if (userRepository.count() == 0 && roomClassRepository.count() == 0) {
            // Insert RoomClass
            RoomClass standardRoomClass = roomClassRepository.save(new RoomClass(RoomClassEnum.STANDARD, 2, 100.0f));
            RoomClass deluxeRoomClass = roomClassRepository.save(new RoomClass(RoomClassEnum.DELUXE, 3, 150.0f));
            RoomClass suiteRoomClass = roomClassRepository.save(new RoomClass(RoomClassEnum.SUITE, 4, 200.0f));

            // Insert Features
            Feature wifi = featureRepository.save(new Feature("WiFi"));
            Feature breakfast = featureRepository.save(new Feature("Breakfast"));

            // Insert RoomClassFeatures
            roomClassFeatureRepository.save(new RoomClassFeature(standardRoomClass, wifi));
            roomClassFeatureRepository.save(new RoomClassFeature(deluxeRoomClass, wifi));
            roomClassFeatureRepository.save(new RoomClassFeature(deluxeRoomClass, breakfast));
            roomClassFeatureRepository.save(new RoomClassFeature(suiteRoomClass, wifi));
            roomClassFeatureRepository.save(new RoomClassFeature(suiteRoomClass, breakfast));

            // Insert RoomStatus
            RoomStatus available = roomStatusRepository.save(new RoomStatus(RoomStatusEnum.AVAILABLE));
            RoomStatus occupied = roomStatusRepository.save(new RoomStatus(RoomStatusEnum.RESERVED));

            // Insert Floors
            Floor firstFloor = floorRepository.save(new Floor(1));
            Floor secondFloor = floorRepository.save(new Floor(2));

            // Fetch managed RoomStatus entities
            RoomStatus availableStatus = roomStatusRepository.findById(available.getId()).orElseThrow();
            RoomStatus occupiedStatus = roomStatusRepository.findById(occupied.getId()).orElseThrow();

            // Insert Rooms
            roomRepository.save(new Room(firstFloor, standardRoomClass, availableStatus, "101"));
            roomRepository.save(new Room(firstFloor, deluxeRoomClass, availableStatus, "102"));
            roomRepository.save(new Room(secondFloor, suiteRoomClass, availableStatus, "201"));

            // Insert Roles
            Role adminRole = roleRepository.save(new Role(UserRole.ADMIN));
            Role userRole = roleRepository.save(new Role(UserRole.CUSTOMER));

            // Insert Users
            UserEntity admin = new UserEntity("admin@example.com", "Admin", "User", "1234567890", passwordEncoder.encode("password"));
            admin.getRoles().add(adminRole);
            userRepository.save(admin);

            UserEntity user = new UserEntity("user@example.com", "Regular", "User", "0987654321", passwordEncoder.encode("password"));
            user.getRoles().add(userRole);
            userRepository.save(user);

            Payment payment = new Payment(PaymentMethod.Cash, PaymentStatus.Paid);
            Payment SavedPayment = paymentRepository.save(payment);

            // Insert Reservations
            Reservation reservation = new Reservation(LocalDate.now(), LocalDate.now().plusDays(3), 2, 0, SavedPayment, user);
            reservationRepository.save(reservation);

            // Insert ReservationRooms
            Room reservedRoom = roomRepository.findById(1L).orElseThrow();
            reservationRoomRepository.save(new ReservationRoom(reservation, reservedRoom));
        }
    }
}
