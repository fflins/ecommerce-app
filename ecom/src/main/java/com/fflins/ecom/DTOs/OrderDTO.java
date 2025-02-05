package com.fflins.ecom.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private LocalDateTime date;
    private List<OrderProductDTO> products;

    // Getters e Setters
}