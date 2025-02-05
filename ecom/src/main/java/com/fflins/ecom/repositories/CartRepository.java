package com.fflins.ecom.repositories;

import com.fflins.ecom.models.Cart;
import com.fflins.ecom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);// Busca pelo objeto User, não pelo ID

}