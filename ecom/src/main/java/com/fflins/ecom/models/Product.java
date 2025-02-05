package com.fflins.ecom.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import javax.management.ConstructorParameters;
import java.math.BigDecimal;
import java.util.List;

//OK
@Entity
@Setter
@Getter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 200)
    @Column
    private String description;

    @NotBlank
    @Size(min = 2, max = 50)
    private String category;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderProduct> orders;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Inventory inventory;

    @Size(max = 255)
    private String imageUrl;

    public Product() {}

    public Product(String name, String description, String category, BigDecimal price){
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public Product(String name, String description, String category, BigDecimal price, String imageUrl){
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.imageUrl=imageUrl;
    }


}
