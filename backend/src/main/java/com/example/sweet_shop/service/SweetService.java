package com.example.sweetshop.service;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.repository.SweetRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.lang.Math;

@Service
public class SweetService {

    private double epsilon = 0.0001;
    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    // Add a new sweet
    public ResponseEntity<Sweet> addSweet(Sweet sweet) {
        Optional<Sweet> existing = sweetRepository.findByNameIgnoreCase(sweet.getName());

        if (existing.isPresent()) {
            // Return a response indicating the sweet already exists
            return ResponseEntity
                    .badRequest()
                    .body(existing.get());
        }

        sweetRepository.save(sweet);
        return ResponseEntity
                .ok(sweet);
    }

    // Get all sweets
    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    // Get one sweet by ID
    public Optional<Sweet> getSweetById(String id) {
        return sweetRepository.findById(id);
    }

    // Update sweet
    public Sweet updateSweet(String id, Sweet sweetDetails) {
        return sweetRepository.findById(id)
                .map(sweet -> {
                    sweet.setName(sweetDetails.getName());
                    sweet.setPrice(sweetDetails.getPrice());
                    sweet.setCategory(sweetDetails.getCategory());
                    sweet.setQuantity(sweetDetails.getQuantity());
                    return sweetRepository.save(sweet);
                })
                .orElseThrow(() -> new RuntimeException("Sweet not found with id " + id));
    }

    // Delete sweet
    public void deleteSweet(String id) {
        sweetRepository.deleteById(id);
    }

    // Search sweets
    public List<Sweet> searchByName(String name) {
        return sweetRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Sweet> searchByPriceRange(double min, double max) {
        return sweetRepository.findByPriceBetween(min - epsilon, max + epsilon);
    }

    public List<Sweet> searchByCategory(String category) {
    return sweetRepository.findByCategory(category);
    }

    public Sweet purchaseSweet(String id,int qunt) {
        Sweet sweet = sweetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sweet not found"));

        if (sweet.getQuantity() <= 0) {
            throw new RuntimeException("Sweet out of stock");
        }

        sweet.setQuantity(sweet.getQuantity() - Math.abs(qunt));
        return sweetRepository.save(sweet);
    }

    public List<Sweet> searchSweets(String name, String category, Double minPrice, Double maxPrice) {
        if (name != null && category != null) {
            return sweetRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(name, category);
        } else if (name != null) {
            return sweetRepository.findByNameContainingIgnoreCase(name);
        } else if (category != null) {
            return sweetRepository.findByCategoryIgnoreCase(category);
        } else if (minPrice != null && maxPrice != null) {

            return sweetRepository.findByPriceBetween(minPrice - epsilon, maxPrice + epsilon);
        } else {
            return sweetRepository.findAll();
        }
    }

    public Sweet restockSweet(String id, int quantity) {
    Sweet sweet = sweetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sweet not found"));

    sweet.setQuantity(sweet.getQuantity() + quantity);
    return sweetRepository.save(sweet);
}


}
