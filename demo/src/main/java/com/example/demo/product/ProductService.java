package com.example.demo.product;

import com.example.demo.product.dto.ProductCreateRequest;
import com.example.demo.product.dto.ProductUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    @Transactional
    public Product create(ProductCreateRequest req) {
        if (repo.existsBySku(req.getSku())) {
            throw new RuntimeException("SKU ya existente: " + req.getSku());
        }

        Product p = Product.builder()
                .sku(req.getSku())
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .stock(req.getStock())
                .build();

        return repo.save(p);
    }

    @Transactional
    public Product update(Long id, ProductUpdateRequest req) {
        Product current = findById(id);

        if (!current.getSku().equalsIgnoreCase(req.getSku()) && repo.existsBySku(req.getSku())) {
            throw new RuntimeException("SKU ya existente: " + req.getSku());
        }

        current.setSku(req.getSku());
        current.setName(req.getName());
        current.setDescription(req.getDescription());
        current.setPrice(req.getPrice());
        current.setStock(req.getStock());

        return repo.save(current);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Producto no encontrado: " + id);
        }
        repo.deleteById(id);
    }
}
