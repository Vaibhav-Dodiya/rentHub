package com.example.rentHub.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rentHub.model.Property;
import com.example.rentHub.repository.PropertyRepository;
import com.example.rentHub.service.PropertyService;
import com.example.rentHub.dto.PropertyRequest;
import java.util.Base64;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PropertyRepository propertyRepository;

    //    @PostMapping("/upload")
//    public ResponseEntity<Property> uploadProperty(
//            @RequestParam("title") String title,
//            @RequestParam("price") double price,
//            @RequestParam("oldPrice") double oldPrice,
//            @RequestParam("delivery") String deliveryInfo,
//            @RequestParam("image") MultipartFile image) {
//
//        Property saved = propertyService.saveProperty(title, price, oldPrice, deliveryInfo, image);
//        return ResponseEntity.ok(saved);
//    }
@PostMapping("/upload")
public ResponseEntity<?> uploadProperty(
        @RequestParam("title") String title,
        @RequestParam("location") String location,
        @RequestParam("price") int price,
        @RequestParam(value = "category", defaultValue = "PROPERTY") String category,
        @RequestParam("images") List<MultipartFile> images) {

    try {
        List<String> urls = new ArrayList<>();

        for (MultipartFile image : images) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("folder", "renthub/properties"));
            urls.add((String) uploadResult.get("secure_url"));
        }

        Property property = new Property();
        property.setTitle(title);
        property.setLocation(location);
        property.setPrice(price);
        property.setCategory(category.toUpperCase());
        property.setImageUrls(urls);

        propertyRepository.save(property);

        return ResponseEntity.ok(property);

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
    }
}

    @GetMapping
    public ResponseEntity<List<Property>> getAll() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Property>> getByCategory(@PathVariable String category) {
        List<Property> properties = propertyRepository.findByCategory(category.toUpperCase());
        return ResponseEntity.ok(properties);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
