package com.example.rentHUB.repository;

import com.example.rentHUB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  //  User findUsername(String username);

    User findByUsername(String username);
}
