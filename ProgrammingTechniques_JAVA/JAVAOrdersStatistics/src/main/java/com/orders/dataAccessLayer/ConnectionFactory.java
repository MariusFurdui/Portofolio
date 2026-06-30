package com.orders.dataAccessLayer;

import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

/**
 * The type Connection factory.
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(
            ConnectionFactory.class.getName());
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASS;

    private static ConnectionFactory singleInstance = new ConnectionFactory();
    private ConnectionFactory() {
        loadProperties();
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Driver not found!", ex);
        }
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(input);
            DRIVER = props.getProperty("db.driver");
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASS = props.getProperty("db.password");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load properties", e);
        }
    }

    private Connection createConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return connection;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Close.
     *
     * @param connection the connection
     */
    public static void close(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Close.
     *
     * @param statement the statement
     */
    public static void close(Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Close.
     *
     * @param resultSet the result set
     */
    public static void close(ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
