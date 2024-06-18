package bzu.edu.hotelManagmentAPI.model;

import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @ManyToOne()
    @JoinColumn(name = "room_class_id")
    private RoomClass roomClass;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "status_id")
    private RoomStatus status;

    @Column(name = "room_number", unique = true)
    private String roomNumber;

    public Room(Floor floor, RoomClass roomClass, RoomStatus status, String roomNumber) {
        this.floor = floor;
        this.roomClass = roomClass;
        this.status = status;
        this.roomNumber = roomNumber;
    }
}
