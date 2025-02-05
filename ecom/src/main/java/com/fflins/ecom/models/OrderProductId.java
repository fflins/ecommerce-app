package com.fflins.ecom.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class OrderProductId implements Serializable {
    private Long orderId;
    private Long productId;

}
