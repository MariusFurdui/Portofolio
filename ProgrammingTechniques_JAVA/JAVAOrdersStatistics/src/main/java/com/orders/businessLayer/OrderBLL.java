package com.orders.businessLayer;

import com.orders.dataAccessLayer.BillDAO;
import com.orders.dataAccessLayer.OrderDAO;
import com.orders.model.Bill;
import com.orders.model.Client;
import com.orders.model.Orders;
import com.orders.model.Product;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Order bll.
 */
public class OrderBLL {
    private OrderDAO orderDAO = new OrderDAO();
    private ProductBLL productBLL = new ProductBLL();
    private ClientBLL clientBLL = new ClientBLL();
    private BillDAO billDAO = new BillDAO();

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Orders> findAll() {
        return orderDAO.findAll();
    }

    /**
     * Find by id orders.
     *
     * @param id the id
     * @return the orders
     */
    public Orders findById(int id) {
        return orderDAO.findById(id);
    }

    /**
     * Insert.
     *
     * @param order the order
     */
    public synchronized void insert(Orders order) {
        Product product = productBLL.findById(order.getProductID());
        if (product == null) {
            throw new IllegalArgumentException("Product not found!");
        }
        if (product.getStock() < order.getQuantity()) {
            throw new IllegalArgumentException("Under-stock! Available: " + product.getStock());
        }

        product.setStock(product.getStock() - order.getQuantity());
        productBLL.update(product);
        int orderId = orderDAO.insert(order);

        Client client = clientBLL.findById(order.getClientID());
        double totalPrice = product.getPrice() * order.getQuantity();

        String orderDate = new java.sql.Timestamp(System.currentTimeMillis()).toString();

        Bill bill = new Bill(0, orderId,
                client != null ? client.getName() : "Unknown",
                product.getName(), order.getQuantity(), totalPrice,
                orderDate);
        billDAO.insert(bill);
    }

    /**
     * Update.
     *
     * @param order the order
     */
    public void update(Orders order) {
        orderDAO.update(order);
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(int id) {
        orderDAO.delete(id);
    }

    /**
     * Find by client id list.
     *
     * @param clientId the client id
     * @return the list
     */
    public List<Orders> findByClientId(int clientId) {
        return orderDAO.findAll().stream()
                .filter(o -> o.getClientID() == clientId)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Gets total quantity ordered.
     *
     * @return the total quantity ordered
     */
    public int getTotalQuantityOrdered() {
        return orderDAO.findAll().stream()
                .mapToInt(Orders::getQuantity)
                .sum();
    }

    /**
     * Gets order date.
     *
     * @param orderId the order id
     * @return the order date
     */
    public String getOrderDate(int orderId) {
        List<Bill> bills = billDAO.findAll();
        for (Bill b : bills) {
            if (b.orderID() == orderId) {
                return b.orderDate();
            }
        }
        return "N/A";
    }
}