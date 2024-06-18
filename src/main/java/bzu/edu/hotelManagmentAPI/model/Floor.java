package bzu.edu.hotelManagmentAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "floor")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "floor_number", nullable = false)
    @Min(value = 1, message = "you should at least have two floors")
    private Integer floorNumber;
    @OneToMany(mappedBy = "floor")
    private Set<Room> rooms;

    public Floor(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
