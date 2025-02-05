package com.fflins.ecom.DTOs;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private List<CartItemDTO> items;
}
