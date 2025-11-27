package com.example.rentHub.service;

import com.example.rentHub.model.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {
    Property saveProperty(String title, int price, int oldPrice,
                          String deliveryInfo, MultipartFile image);
    Property savePropertyFromJson(
            String title,
            int price,
            int oldPrice,
            String deliveryInfo,
            String discount,
            String uploadedBy,
            byte[] imageBytes
    );
    List<Property> getAllProperties();
    void deleteProperty(String id);
}
