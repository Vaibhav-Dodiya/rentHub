package com.example.rentHub.service;

import com.example.rentHub.model.Property;
import com.example.rentHub.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    private final String uploadDir = "uploads/";
    

    @Override
    public Property saveProperty(String title, double price, double oldPrice,
                                 String deliveryInfo, MultipartFile image) {
        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());

            Property property = new Property();
            property.setTitle(title);
            property.setPrice(price);
            property.setOldPrice(oldPrice);
            property.setDiscount("New");
            property.setDeliveryInfo(deliveryInfo);
            property.setImageUrls(List.of("/" + uploadDir + fileName));

            return propertyRepository.save(property);
        } catch (IOException e) {
            throw new RuntimeException("Error saving property", e);
        }
    }

    @Override
    public Property savePropertyFromJson(String title, double price, double oldPrice,
                                         String deliveryInfo, String discount, String uploadedBy,
                                         byte[] imageBytes) {

        try {
            // SAVE IMAGE LOCALLY
            String fileName = System.currentTimeMillis() + ".jpg";
            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, imageBytes);

            // Create Property object
            Property property = new Property();
            property.setTitle(title);
            property.setPrice(price);
            property.setOldPrice(oldPrice);
            property.setDeliveryInfo(deliveryInfo);
            property.setDiscount(discount);
            property.setUploadedBy(uploadedBy);
            property.setImageUrls(List.of("/uploads/" + fileName));
            property.setCreatedAt(LocalDateTime.now());

            return propertyRepository.save(property);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

     @Override
    public void deleteProperty(String id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found: " + id);
        }
        propertyRepository.deleteById(id);
    }
}
