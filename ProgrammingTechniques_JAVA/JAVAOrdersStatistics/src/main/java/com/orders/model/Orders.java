package com.orders.model;

/**
 * The type Orders.
 */
public class Orders {
    private int idOrder;
    private int clientID;
    private int productID;
    private int quantity;

    /**
     * Instantiates a new Orders.
     *
     * @param id        the id
     * @param clientId  the client id
     * @param productId the product id
     * @param quantity  the quantity
     */
    public Orders(int id, int clientId, int productId, int quantity) {
        this.idOrder = id;
        this.clientID = clientId;
        this.productID = productId;
        this.quantity = quantity;
    }

    /**
     * Instantiates a new Orders.
     */
    public Orders() {
        this.idOrder = 0;
        this.clientID = 0;
        this.productID = 0;
        this.quantity = 0;
    }

    /**
     * Gets id order.
     *
     * @return the id order
     */
    public int getIdOrder() {
        return idOrder;
    }

    /**
     * Sets id order.
     *
     * @param idOrder the id order
     */
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * Gets client id.
     *
     * @return the client id
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Sets client id.
     *
     * @param clientID the client id
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * Gets product id.
     *
     * @return the product id
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Sets product id.
     *
     * @param productID the product id
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
