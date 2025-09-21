package com.example.sweetshop.repository;

import com.example.sweetshop.model.Sweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SweetRepository extends MongoRepository<Sweet, String> {

    // Custom query methods (Spring builds queries automatically)
    Optional<Sweet> findByNameIgnoreCase(String name);
    List<Sweet> findByNameContainingIgnoreCase(String name);
     // Search by category
    List<Sweet> findByCategoryIgnoreCase(String category);
    List<Sweet> findByPriceBetween(double minPrice, double maxPrice);
    List<Sweet> findByCategory(String category); 
    List<Sweet> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(String name, String category);
}
