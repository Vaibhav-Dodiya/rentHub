package com.example.rentHub.repository;

import com.example.rentHub.model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByOwnerId(String ownerId);
    List<Request> findByRequesterId(String requesterId);
    List<Request> findByPropertyId(String propertyId);
    boolean existsByPropertyIdAndRequesterId(String propertyId, String requesterId);
}
