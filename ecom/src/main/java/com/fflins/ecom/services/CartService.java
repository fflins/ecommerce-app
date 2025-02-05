package com.fflins.ecom.services;

import com.fflins.ecom.models.Cart;
import com.fflins.ecom.models.CartItem;
import com.fflins.ecom.models.Product;
import com.fflins.ecom.models.User;
import com.fflins.ecom.repositories.CartRepository;
import com.fflins.ecom.repositories.ProductRepository;
import com.fflins.ecom.repositories.UserRepository;
import com.fflins.ecom.DTOs.AddItemRequest;
import com.fflins.ecom.DTOs.CartDTO;
import com.fflins.ecom.DTOs.CartItemDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CartDTO addItemToCart(Long userId, AddItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Cart cart = getCartByUser(userId);

        // Se o carrinho não existir, cria um novo
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<CartItem>());
            user.setCart(cart);
            cartRepository.save(cart);
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Verifica se o produto já existe no carrinho
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.productId()))
                .findFirst()
                .orElse(null);

        // Se o produto já existe no carrinho, apenas atualiza a quantidade
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
        } else {
            // Caso contrário, cria um novo item no carrinho
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(request.quantity());
            item.setCart(cart);
            cart.getItems().add(item);
        }

        cartRepository.save(cart);

        // Converte o Cart para CartDTO e retorna
        return convertToDTO(cart);
    }


    @Transactional
    public void removeItemFromCart(Long userId, Long itemId) {
        Cart cart = getCartByUser(userId);
        if (cart == null) {
            throw new RuntimeException("Carrinho não encontrado");
        }
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    public Cart getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return user.getCart();
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUser(userId);
        if (cart == null) {
            throw new RuntimeException("Carrinho não encontrado");
        }
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Método para converter Cart em CartDTO
    private CartDTO convertToDTO(Cart cart) {
        List<CartItemDTO> cartItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setProductImageUrl(item.getProduct().getImageUrl());
            itemDTO.setProductPrice(item.getProduct().getPrice());
            cartItems.add(itemDTO);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setItems(cartItems);
        return cartDTO;
    }
}
