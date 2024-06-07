package bzu.edu.hotelManagmentAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "room_class")
public class RoomClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", nullable = false)
    @NotBlank(message = "class name is required")
    @Size(max = 30, message = "class name must be at most 30 characters")
    private String className;
    @Column(name = "num_beds")
    private Integer numBeds;
    @Column(nullable = false)
    @Min(value = 1, message = "Price must be greater than 0")
    private Float price;

    public RoomClass(String className, Integer numBeds, Float price) {
        this.className = className;
        this.numBeds = numBeds;
        this.price = price;
    }
}
