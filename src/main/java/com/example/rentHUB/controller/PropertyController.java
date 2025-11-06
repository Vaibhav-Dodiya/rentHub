package com.example.rentHUB.controller;

import com.example.rentHUB.model.Property;
import com.example.rentHUB.service.PropertyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/upload")
    public ResponseEntity<Property> uploadProperty(
            @RequestParam("title") String title,
            @RequestParam("price") double price,
            @RequestParam("oldPrice") double oldPrice,
            @RequestParam("delivery") String deliveryInfo,
            @RequestParam("image") MultipartFile image) {

        Property saved = propertyService.saveProperty(title, price, oldPrice, deliveryInfo, image);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAll() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
