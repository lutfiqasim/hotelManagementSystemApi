package bzu.edu.hotelManagmentAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "room_class")
public class RoomClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name",nullable = false)
    @NotBlank(message = "class name is required")
    @Size(max = 30, message = "class name must be at most 30 characters")
    private String className;

    @Column(nullable = false)
    @Min(value = 1, message = "Price must be greater than 0")
    private Float price;
}
