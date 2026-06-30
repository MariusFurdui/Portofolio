package com.orders.dataAccessLayer;

import com.orders.model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Bill dao.
 */
public class BillDAO {
    private static final Logger LOGGER = Logger.getLogger(BillDAO.class.getName());

    /**
     * Insert int.
     *
     * @param bill the bill
     * @return the int
     */
    public int insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        String query = "INSERT INTO bill (orderID, clientName, productName, quantity, totalPrice, orderDate) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, bill.orderID());
            statement.setString(2, bill.clientName());
            statement.setString(3, bill.productName());
            statement.setInt(4, bill.quantity());
            statement.setDouble(5, bill.totalPrice());
            statement.setString(6, bill.orderDate());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating bill failed, no rows affected");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating bill failed, no ID obtained");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO: insert " + e.getMessage());
            return 0;
        } finally {
            ConnectionFactory.close(generatedKeys);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM bill";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getInt("idBill"),
                        resultSet.getInt("orderID"),
                        resultSet.getString("clientName"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("totalPrice"),
                        resultSet.getString("orderDate")
                );
                bills.add(bill);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO: findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bills;
    }
}