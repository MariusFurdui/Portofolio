package com.orders.model;

/**
 * The type Bill.
 */
public record Bill(int idBill, int orderID, String clientName,
                   String productName, int quantity, double totalPrice, String orderDate) {
    /**
     * Instantiates a new Bill.
     *
     * @param idBill      the id bill
     * @param orderID     the order id
     * @param clientName  the client name
     * @param productName the product name
     * @param quantity    the quantity
     * @param totalPrice  the total price
     * @param orderDate   the order date
     */
    public Bill {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
    }
}
