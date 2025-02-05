package com.fflins.ecom.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderProductDTO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;

}
