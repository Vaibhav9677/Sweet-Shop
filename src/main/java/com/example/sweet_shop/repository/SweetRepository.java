package com.example.sweetshop.repository;

import com.example.sweetshop.model.Sweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SweetRepository extends MongoRepository<Sweet, String> {

    // Custom query methods (Spring builds queries automatically)
    List<Sweet> findByNameContainingIgnoreCase(String name);
    List<Sweet> findByPriceBetween(double minPrice, double maxPrice);
    List<Sweet> findByCategory(String category); // we’ll add category field later
}
