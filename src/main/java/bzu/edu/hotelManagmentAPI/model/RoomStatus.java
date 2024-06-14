package bzu.edu.hotelManagmentAPI.model;

import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_status")
public class RoomStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_name", length = 50)
    @NotBlank(message = "status name must be included")
    private RoomStatusEnum statusName;

    public RoomStatus(RoomStatusEnum statusName) {
        this.statusName = statusName;
    }
}
