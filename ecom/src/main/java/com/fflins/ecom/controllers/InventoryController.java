package com.fflins.ecom.controllers;

import com.fflins.ecom.DTOs.InventoryDTO;
import com.fflins.ecom.DTOs.UpdateStockRequest;
import com.fflins.ecom.models.Inventory;
import com.fflins.ecom.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired private InventoryService inventoryService;

    @PutMapping("/products/{productId}")
    public InventoryDTO updateStock(@PathVariable Long productId, @RequestBody UpdateStockRequest request) {
        System.out.println("product id update stock" + productId);
        System.out.println("request" + request);
        System.out.println("request data " + request.quantity());
        Inventory inventory = inventoryService.updateStock(productId, request); // Passando o request completo
        InventoryDTO dto = new InventoryDTO();
        dto.setProductId(inventory.getProduct().getId());
        dto.setStock(inventory.getStock());
        return dto;
    }

    @GetMapping
    public List<InventoryDTO> getAllInventory() {
        return inventoryService.getAllInventory().stream().map(inventory -> {
            InventoryDTO dto = new InventoryDTO();
            dto.setProductId(inventory.getProduct().getId());
            dto.setStock(inventory.getStock());
            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{productId}")
    public InventoryDTO getInventory(@PathVariable Long productId) {
        Inventory inventory = inventoryService.getInventory(productId);
        InventoryDTO dto = new InventoryDTO();
        dto.setProductId(inventory.getProduct().getId());
        dto.setStock(inventory.getStock());
        return dto;
    }
}
