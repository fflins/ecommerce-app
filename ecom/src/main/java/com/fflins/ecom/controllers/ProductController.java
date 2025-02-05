package com.fflins.ecom.controllers;

import com.fflins.ecom.DTOs.ProductDTO;
import com.fflins.ecom.models.Product;
import com.fflins.ecom.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return convertToDTO(product);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setPrice(product.getPrice());
        productDTO.setImageURL(product.getImageUrl());
        return productDTO;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") BigDecimal price,
            @RequestParam("file") MultipartFile file) {

        try {
            // Criando diretório de uploads, caso não exista
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Gera um nome de arquivo único
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Salva a imagem no servidor
            Files.write(filePath, file.getBytes());

            // Cria o produto com o caminho da imagem
            productService.addProduct(name, description, category, price, file);

            return ResponseEntity.ok("Produto criado com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar a imagem: " + e.getMessage());
        }

    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Verifica se o produto existe
            Product product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.status(404).body("Produto não encontrado.");
            }

            // Atualiza as propriedades do produto com os novos dados
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);

            // Se um arquivo de imagem foi enviado, salva-o
            if (file != null && !file.isEmpty()) {
                // Cria o diretório de uploads, caso não exista
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Gera um nome de arquivo único
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);

                // Salva a nova imagem no servidor
                Files.write(filePath, file.getBytes());

                // Atualiza a URL da imagem do produto
                product.setImageUrl(filePath.toString());
            }

            // Salva as alterações do produto
            productService.updateProduct(product, file );

            return ResponseEntity.ok("Produto atualizado com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar a imagem: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Erro ao atualizar produto: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Produto deletado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao deletar produto: " + e.getMessage());
        }
    }

}
