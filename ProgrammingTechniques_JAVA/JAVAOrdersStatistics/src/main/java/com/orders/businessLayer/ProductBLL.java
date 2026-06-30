package com.orders.businessLayer;

import com.orders.dataAccessLayer.ProductDAO;
import com.orders.model.Product;
import java.util.List;

/**
 * The type Product bll.
 */
public class ProductBLL {
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    /**
     * Find by id product.
     *
     * @param id the id
     * @return the product
     */
    public Product findById(int id) {
        return productDAO.findById(id);
    }

    /**
     * Insert.
     *
     * @param product the product
     */
    public void insert(Product product) {
        productDAO.insert(product);
    }

    /**
     * Update.
     *
     * @param product the product
     */
    public void update(Product product) {
        productDAO.update(product);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(int id) {
        productDAO.delete(id);
    }

    /**
     * Find by min stock list.
     *
     * @param minStock the min stock
     * @return the list
     */
    public List<Product> findByMinStock(int minStock) {
        return productDAO.findAll().stream()
                .filter(p -> p.getStock() >= minStock)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Gets total stock value.
     *
     * @return the total stock value
     */
    public double getTotalStockValue() {
        return productDAO.findAll().stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();
    }
}