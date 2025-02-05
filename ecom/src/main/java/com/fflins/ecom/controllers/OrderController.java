package com.fflins.ecom.controllers;

import com.fflins.ecom.DTOs.OrderDTO;
import com.fflins.ecom.models.Order;
import com.fflins.ecom.models.User;
import com.fflins.ecom.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping
    public Order createOrder() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Order Controller Usuário autenticado: " + currentUser.getUsername() + currentUser.getId());
        return orderService.createOrder(currentUser.getId());
    }


    @GetMapping
    public List<OrderDTO> getUserOrders() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderService.getOrderByUserId(currentUser.getId());
    }
}
