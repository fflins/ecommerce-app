package com.fflins.ecom.services;

import com.fflins.ecom.DTOs.OrderDTO;
import com.fflins.ecom.DTOs.OrderProductDTO;
import com.fflins.ecom.models.*;
import com.fflins.ecom.repositories.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @Transactional
    public Order createOrder(Long userId) {
        System.out.println("Primeiro order " + userId);
        Cart cart = cartService.getCartByUser(userId);
        System.out.println("Segundo order " + userId);

        Order order = new Order();
        order.setUser(userRepository.findById(userId).orElse(null));
        System.out.println("Order USER " + userId);
        order.setDate(LocalDateTime.now());

        // 🔹 Salvar a Order primeiro para garantir um ID válido
        order = orderRepository.save(order);
        System.out.println("Order salva com ID: " + order.getId());

        List<OrderProduct> orderProducts = new ArrayList<>();

        Order finalOrder = order;
        cart.getItems().forEach(cartItem -> {
            OrderProduct orderProduct = new OrderProduct();
            OrderProductId orderProductId = new OrderProductId();

            orderProductId.setOrderId(finalOrder.getId()); // Agora o ID não será null
            orderProductId.setProductId(cartItem.getProduct().getId());

            orderProduct.setId(orderProductId);
            orderProduct.setProduct(cartItem.getProduct());
            orderProduct.setQuantity(cartItem.getQuantity());
            orderProduct.setPrice(cartItem.getProduct().getPrice());
            orderProduct.setOrder(finalOrder);

            orderProducts.add(orderProduct);
        });

        // 🔹 Salvar os OrderProducts na tabela correspondente
        orderProductRepository.saveAll(orderProducts);

        // 🔹 Atualizar a lista de produtos na Order e salvar novamente
        order.setProducts(orderProducts);
        order = orderRepository.save(order);

        updateStock(order); // Atualiza o estoque após salvar
        cartService.clearCart(userId);

        return order;
    }


    public List<OrderDTO> getOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setDate(order.getDate());
        dto.setProducts(order.getProducts().stream()
                .map(this::convertToOrderProductDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private OrderProductDTO convertToOrderProductDTO(OrderProduct op) {
        OrderProductDTO dto = new OrderProductDTO();
        dto.setProductId(op.getProduct().getId());
        dto.setProductName(op.getProduct().getName());
        dto.setPrice(op.getPrice());
        dto.setQuantity(op.getQuantity());
        return dto;
    }

    private void updateStock(Order order) {
        order.getProducts().forEach(op -> {
            Inventory inventory = inventoryRepository.findByProduct(op.getProduct()).orElse(null);

            if (inventory == null) {
                log.error("Inventory not found for product: " + op.getProduct().getName());
                return;
            }

            log.debug("Inventory id: " + inventory.getId());
            log.debug("Produto do inventory: " + inventory.getProduct().getName());
            log.debug("Stock atual: " + inventory.getStock());

            int newStock = inventory.getStock() - op.getQuantity();
            if (newStock < 0) {
                log.error("Erro: Estoque insuficiente para o produto " + op.getProduct().getName());
                throw new IllegalArgumentException("Estoque insuficiente para o produto " + op.getProduct().getName());
            }

            inventory.setStock(newStock);
            inventoryRepository.save(inventory);
        });
    }

}
