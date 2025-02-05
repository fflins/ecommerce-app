package com.fflins.ecom.services;

import com.fflins.ecom.models.Inventory;
import com.fflins.ecom.models.Product;
import com.fflins.ecom.repositories.InventoryRepository;
import com.fflins.ecom.repositories.ProductRepository;
import com.fflins.ecom.DTOs.UpdateStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private ProductRepository productRepository;

    public Inventory updateStock(Long productId, UpdateStockRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Inventário do produto não encontrado"));

        System.out.println("request:" + request);
        System.out.println("Stock antes:" + inventory.getStock());
        inventory.setStock(inventory.getStock() + request.quantity());
        System.out.println("Stock dps:" + inventory.getStock());
        return inventoryRepository.save(inventory);
    }


    public Inventory getInventory(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Inventário não encontrado"));
    }

    public List<Inventory> getAllInventory() {
        List<Inventory> allInventories = inventoryRepository.findAll();
        return allInventories.stream()
                .filter(inventory -> inventory.getStock() > 0)
                .collect(Collectors.toList());
    }
}
