package com.example.rentHub.controller;

import com.example.rentHub.model.Request;
import com.example.rentHub.model.Property;
import com.example.rentHub.model.User;
import com.example.rentHub.repository.RequestRepository;
import com.example.rentHub.repository.PropertyRepository;
import com.example.rentHub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new request
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, String> requestData) {
        try {
            String propertyId = requestData.get("propertyId");
            String requesterId = requestData.get("requesterId");
            String message = requestData.get("message");

            // Get property details
            Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
            if (!propertyOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Property not found"));
            }
            Property property = propertyOpt.get();

            // Get requester details
            Optional<User> requesterOpt = userRepository.findById(requesterId);
            if (!requesterOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Requester not found"));
            }
            User requester = requesterOpt.get();

            // Get owner details
            Optional<User> ownerOpt = userRepository.findById(property.getUserId());
            if (!ownerOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Owner not found"));
            }
            User owner = ownerOpt.get();

            // Create request
            Request request = new Request();
            request.setPropertyId(propertyId);
            request.setPropertyTitle(property.getTitle());
            request.setPropertyCategory(property.getCategory());
            request.setOwnerId(property.getUserId());
            request.setOwnerName(owner.getUsername());
            request.setRequesterId(requesterId);
            request.setRequesterName(requester.getUsername());
            request.setRequesterEmail(requester.getEmail());
            request.setMessage(message);

            Request savedRequest = requestRepository.save(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Request sent successfully");
            response.put("request", savedRequest);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating request: " + e.getMessage()));
        }
    }

    // Get all requests for an owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getOwnerRequests(@PathVariable String ownerId) {
        try {
            List<Request> requests = requestRepository.findByOwnerId(ownerId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching requests: " + e.getMessage()));
        }
    }

    // Get all requests made by a customer
    @GetMapping("/customer/{requesterId}")
    public ResponseEntity<?> getCustomerRequests(@PathVariable String requesterId) {
        try {
            List<Request> requests = requestRepository.findByRequesterId(requesterId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching requests: " + e.getMessage()));
        }
    }

    // Update request status (accept/reject)
    @PutMapping("/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(
            @PathVariable String requestId,
            @RequestBody Map<String, String> statusData) {
        try {
            Optional<Request> requestOpt = requestRepository.findById(requestId);
            if (!requestOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Request not found"));
            }

            Request request = requestOpt.get();
            request.setStatus(statusData.get("status"));
            requestRepository.save(request);

            return ResponseEntity.ok(Map.of("message", "Request status updated", "request", request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error updating request: " + e.getMessage()));
        }
    }

    // Delete a request
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable String requestId) {
        try {
            if (!requestRepository.existsById(requestId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Request not found"));
            }
            requestRepository.deleteById(requestId);
            return ResponseEntity.ok(Map.of("message", "Request deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error deleting request: " + e.getMessage()));
        }
    }
}
