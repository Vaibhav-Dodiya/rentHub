package com.example.rentHUB.controller;

import com.example.rentHUB.model.Property;
import com.example.rentHUB.service.PropertyService;
import com.example.rentHUB.dto.PropertyRequest;
import java.util.Base64;

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
@PostMapping("/upload-json")
public ResponseEntity<Property> uploadJson(@RequestBody PropertyRequest req) {

    // Decode Base64 image
    String b64 = req.getImageBase64();
    if (b64 == null || b64.isBlank()) {
        return ResponseEntity.badRequest().build();
    }
    // strip data URI prefix if present: data:<mediatype>;base64,XXXXX
    int comma = b64.indexOf(',');
    if (comma > -1) {
        b64 = b64.substring(comma + 1);
    }
    // remove whitespace/newlines
    b64 = b64.replaceAll("\\s+","");

    byte[] imageBytes;
    try {
        imageBytes = Base64.getDecoder().decode(b64);
    } catch (IllegalArgumentException ex) {
        // try URL-safe decoder as a fallback (accepts '-' and '_' instead of '+' and '/')
        try {
            imageBytes = Base64.getUrlDecoder().decode(b64);
        } catch (IllegalArgumentException ex2) {
            return ResponseEntity.badRequest().build();
        }
    }

    Property saved = propertyService.savePropertyFromJson(
            req.getTitle(),
            req.getPrice(),
            req.getOldPrice(),
            req.getDeliveryInfo(),
            req.getDiscount(),
            req.getUploadedBy(),
            imageBytes
    );

    return ResponseEntity.ok(saved);
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
