package com.example.rentHUB.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rentHUB.model.Property;
import com.example.rentHUB.repository.PropertyRepository;
import com.example.rentHUB.service.PropertyService;
import com.example.rentHUB.dto.PropertyRequest;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
