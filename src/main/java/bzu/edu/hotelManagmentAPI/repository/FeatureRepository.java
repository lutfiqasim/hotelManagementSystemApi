package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
}
