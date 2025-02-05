package com.fflins.ecom.controllers;

import com.fflins.ecom.DTOs.AddItemRequest;
import com.fflins.ecom.DTOs.CartDTO;
import com.fflins.ecom.DTOs.CartItemDTO;
import com.fflins.ecom.models.User;
import com.fflins.ecom.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;

    @GetMapping
    public CartDTO getCurrentUserCart() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CartItemDTO> cartItems = cartService.getCartByUser(currentUser.getId())
                .getItems().stream().map(item -> {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setId(item.getId());
                    dto.setProductId(item.getProduct().getId());
                    dto.setQuantity(item.getQuantity());
                    dto.setProductName(item.getProduct().getName());
                    dto.setProductImageUrl(item.getProduct().getImageUrl());
                    dto.setProductPrice(item.getProduct().getPrice());
                    return dto;
                }).collect(Collectors.toList());

        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(cartItems);

        return cartDTO;
    }

    @PostMapping("/items")
    public CartDTO addItemToCart(@RequestBody AddItemRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User currentUser = (User) principal;
            return cartService.addItemToCart(currentUser.getId(), request);
        } else {
            throw new RuntimeException("Erro: Verifique se esta logado");
        }
    }

    @DeleteMapping("/items/{itemId}")
    public void removeItemFromCart(@PathVariable Long itemId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartService.removeItemFromCart(currentUser.getId(), itemId);
    }

    @PostMapping("/clear")
    public void clearCart() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartService.clearCart(currentUser.getId());
    }
}
