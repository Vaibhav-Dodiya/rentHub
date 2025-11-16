package com.example.rentHUB.service;

import com.example.rentHUB.model.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {
    Property saveProperty(String title, double price, double oldPrice,
                          String deliveryInfo, MultipartFile image);
    Property savePropertyFromJson(
            String title,
            double price,
            double oldPrice,
            String deliveryInfo,
            String discount,
            String uploadedBy,
            byte[] imageBytes
    );
    List<Property> getAllProperties();
    void deleteProperty(String id);
}
