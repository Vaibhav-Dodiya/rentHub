//package com.example.rentHUB.service;
//
//import com.example.rentHUB.repository.PropertyRepository;
//import org.hibernate.mapping.Property;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import com.example.rentHUB.entity.Property;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//    @Service
//    public class PropertyServiceImpl implements PropertyService {
//
//        @Autowired
//        private PropertyRepository propertyRepository;
//
//        private final String uploadDir = "uploads/";
//
//        @Override
//        public Property saveProperty(String title, double price, double oldPrice,
//                                     String deliveryInfo, MultipartFile image) {
//            try {
//                // Save image to folder
//                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir, fileName);
//                Files.createDirectories(filePath.getParent());
//                Files.write(filePath, image.getBytes());
//
//                // Build Property
//                Property property = new Property();
//                property.setTitle(title);
//                property.setPrice(price);
//                property.setOldPrice(oldPrice);
//                property.setDiscount("New");
//                property.setDeliveryInfo(deliveryInfo);
//                property.setImageUrl("/" + uploadDir + fileName);
//
//                return propertyRepository.save(property);
//
//            } catch (IOException e) {
//                throw new RuntimeException("Error saving property", e);
//            }
//        }
//
//        @Override
//        public List<Property> getAllProperties() {
//            return propertyRepository.findAll();
//        }
//
//        @Override
//        public void deleteProperty(Long id) {
//            propertyRepository.deleteById(id);
//        }
//}
//package com.example.rentHUB.service;
//
//import com.example.rentHUB.entity.Property;
//import com.example.rentHUB.repository.PropertyRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@Service
//public class PropertyServiceImpl implements PropertyService {
//
//    @Autowired
//    private PropertyRepository propertyRepository;
//
//    private final String uploadDir = "uploads/";
//
//    @Override
//    public Property saveProperty(String title, double price, double oldPrice,
//                                 String deliveryInfo, MultipartFile image) {
//        try {
//            // Save image to folder
//            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//            Path filePath = Paths.get(uploadDir, fileName);
//            Files.createDirectories(filePath.getParent());
//            Files.write(filePath, image.getBytes());
//
//            // Build Property entity
//            Property property = new Property();
//            property.setTitle(title);
//            property.setPrice(price);
//            property.setOldPrice(oldPrice);
//            property.setDiscount("New");
//            property.setDeliveryInfo(deliveryInfo);
//            property.setImageUrl("/" + uploadDir + fileName);
//
//            return propertyRepository.save(property);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Error saving property", e);
//        }
//    }
//
//    @Override
//    public List<Property> getAllProperties() {
//        return propertyRepository.findAll();
//    }
//
//    @Override
//    public void deleteProperty(Long id) {
//        propertyRepository.deleteById(id);
//    }
//}
package com.example.rentHUB.service;

import com.example.rentHUB.model.Property;
import com.example.rentHUB.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    private final String uploadDir = "uploads/";
   // @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
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
            property.setImageUrl("/" + uploadDir + fileName);

            return propertyRepository.save(property);
        } catch (IOException e) {
            throw new RuntimeException("Error saving property", e);
        }
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public void deleteProperty(String id) {
        if (propertyRepository.existsById(id)) {
            propertyRepository.deleteById(id);
        } else {
            throw new RuntimeException("Property not found with id: " + id);
        }
    }
}
