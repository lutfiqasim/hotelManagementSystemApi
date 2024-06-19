package bzu.edu.hotelManagmentAPI.service.FeatureServiceImp;

import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.Feature;
import bzu.edu.hotelManagmentAPI.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImp {
    private FeatureRepository featureRepository;

    @Autowired
    public FeatureServiceImp(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    public Feature saveFeature(Feature feature) {
        return featureRepository.save(feature);
    }

    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    public Feature getFeatureById(Long id) {
        return featureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("feature with id: " + id + ", not found"));
    }

    public void deleteFeature(Long id) {
        featureRepository.deleteById(id);
    }
}