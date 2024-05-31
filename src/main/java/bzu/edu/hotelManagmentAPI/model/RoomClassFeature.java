package bzu.edu.hotelManagmentAPI.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "room_class_feature")
public class RoomClassFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_class_id")
    private RoomClass roomClass;
    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;
}
