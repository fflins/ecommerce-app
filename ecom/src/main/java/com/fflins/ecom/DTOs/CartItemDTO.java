package com.fflins.ecom.DTOs;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private String productName;
    private String productImageUrl;
    private BigDecimal productPrice;
}
