package com.fflins.ecom.repositories;

import com.fflins.ecom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String username);
    Optional<User> findByEmail(String email);
}