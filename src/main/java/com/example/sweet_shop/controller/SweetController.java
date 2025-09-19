package com.example.sweetshop.controller;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.service.SweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    private final SweetService sweetService;

    public SweetController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    // POST /api/sweets → add new sweet
    @PostMapping
    public ResponseEntity<Sweet> addSweet(@RequestBody Sweet sweet) {
        return ResponseEntity.ok(sweetService.addSweet(sweet));
    }

    // GET /api/sweets → list all
    @GetMapping
    public ResponseEntity<List<Sweet>> getAllSweets() {
        return ResponseEntity.ok(sweetService.getAllSweets());
    }

    // GET /api/sweets/{id} → get by id
    @GetMapping("/{id}")
    public ResponseEntity<Sweet> getSweetById(@PathVariable String id) {
        return sweetService.getSweetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/sweets/{id} → update
    @PutMapping("/{id}")
    public ResponseEntity<Sweet> updateSweet(@PathVariable String id, @RequestBody Sweet sweet) {
        return ResponseEntity.ok(sweetService.updateSweet(id, sweet));
    }

    // DELETE /api/sweets/{id} → delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSweet(@PathVariable String id) {
        sweetService.deleteSweet(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/sweets/category/{category}
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Sweet>> getSweetsByCategory(@PathVariable String category) {
    return ResponseEntity.ok(sweetService.searchByCategory(category));
    }

    // GET /api/sweets/search?name=lado&minPrice=10&maxPrice=50
    @GetMapping("/search")
    public ResponseEntity<List<Sweet>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
            
        if((name != null) && (category != null) && (minPrice != null) && (maxPrice != null))
        {
            return ResponseEntity.ok(sweetService.searchSweets(name, category, minPrice, maxPrice));
        }
        else if (name != null) {
            return ResponseEntity.ok(sweetService.searchByName(name));
        } else if (minPrice != null && maxPrice != null) {
            return ResponseEntity.ok(sweetService.searchByPriceRange(minPrice, maxPrice));
        } 
        else if (category != null) {
            return ResponseEntity.ok(sweetService.searchByCategory(category));
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/purchase/{qunt}")
    public Sweet purchaseSweet(@PathVariable String id,@PathVariable int qunt) {
    return sweetService.purchaseSweet(id,qunt);
    }
}
