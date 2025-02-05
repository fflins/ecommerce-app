package com.fflins.ecom.services;

import com.fflins.ecom.models.Inventory;
import com.fflins.ecom.models.Product;
import com.fflins.ecom.repositories.InventoryRepository;
import com.fflins.ecom.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    private static final String UPLOAD_DIR = "uploads/"; // Diretório de armazenamento das imagens

    // Obtém todos os produtos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Obtém um produto pelo ID
    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    // Adiciona um produto com imagem
    public Product addProduct(String name, String description, String category,
                              BigDecimal price, MultipartFile file) {
        try {
            String imageUrl = null;

            // Faz o upload da imagem antes de salvar o produto
            if (file != null && !file.isEmpty()) {
                imageUrl = saveImage(file);
            }

            // Cria o objeto produto com a URL da imagem
            Product product = new Product(name, description, category, price, imageUrl);

            // Salva o produto no banco de dados
            productRepository.save(product);

            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setStock(0);
            inventoryRepository.save(inventory);

            return product;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar o produto: " + e.getMessage());
        }
    }

    // Atualiza um produto existente (com opção de nova imagem)
    public Product updateProduct(Product updatedProduct, MultipartFile file) {
        Product existingProduct = productRepository.findById(updatedProduct.getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Se houver uma nova imagem, substitui a existente
        if (file != null && !file.isEmpty()) {
            String newImageUrl = saveImage(file);
            existingProduct.setImageUrl(newImageUrl);
        }

        // Atualiza os outros campos
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());

        return productRepository.save(existingProduct);
    }

    // Deleta um produto
    public void deleteProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto inexistente"));

        productRepository.delete(product);
    }

    // Salva a imagem no diretório "uploads/" e retorna a URL
    private String saveImage(MultipartFile file) {
        try {
            // Cria diretório caso não exista
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Gera um nome único para a imagem usando UUID
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Salva o arquivo no diretório
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName; // Retorna a URL relativa

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar a imagem", e);
        }
    }
}
