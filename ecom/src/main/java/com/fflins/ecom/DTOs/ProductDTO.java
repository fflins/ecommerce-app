package com.fflins.ecom.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String imageURL;
}
