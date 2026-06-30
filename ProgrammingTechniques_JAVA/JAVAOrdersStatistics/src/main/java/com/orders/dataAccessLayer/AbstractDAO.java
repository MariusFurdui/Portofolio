package com.orders.dataAccessLayer;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 * The type Abstract dao.
 *
 * @param <T> the type parameter
 */
public class AbstractDAO<T> {
    private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    /**
     * Instantiates a new Abstract dao.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType)
                getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    private List<T> createObjects(ResultSet rs) {
        List<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = rs.getObject(field.getName());
                    field.set(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error creating objects: " + e.getMessage());
        }
        return list;
    }

    private String getIdFieldName() {
        return type.getDeclaredFields()[0].getName();
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName().toLowerCase();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: findAll " + ex.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return new ArrayList<>();
    }

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName().toLowerCase() + " WHERE " + getIdFieldName() + " = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<T> list = createObjects(resultSet);
            if (!list.isEmpty()) return list.get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Insert.
     *
     * @param t the t
     * @return the int
     */
    public int insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        Field[] fields = type.getDeclaredFields();
        StringBuilder cols = new StringBuilder("INSERT INTO " + type.getSimpleName().toLowerCase() + " (");
        StringBuilder vals = new StringBuilder("VALUES (");
        for (Field field : fields) {
            if (!field.getName().equals(getIdFieldName())) {
                cols.append(field.getName()).append(", ");
                vals.append("?, ");
            }
        }
        cols = new StringBuilder(cols.toString().replaceAll(", $", ") "));
        vals = new StringBuilder(vals.toString().replaceAll(", $", ")"));
        String finalQuery = cols + vals.toString();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(finalQuery, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Field field : fields) {
                if (!field.getName().equals(getIdFieldName())) {
                    field.setAccessible(true);
                    statement.setObject(index++, field.get(t));
                }
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException | IllegalAccessException ex) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: insert " + ex.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return 0;
    }

    /**
     * Update.
     *
     * @param t the t
     */
    public void update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        Field[] fields = type.getDeclaredFields();
        StringBuilder query = new StringBuilder("UPDATE " + type.getSimpleName().toLowerCase() + " SET ");
        for (Field field : fields) {
            if (!field.getName().equals(getIdFieldName())) {
                query.append(field.getName()).append(" = ?, ");
            }
        }
        query = new StringBuilder(query.toString().replaceAll(", $", " WHERE " + getIdFieldName() + " = ?"));
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());
            int index = 1;
            int idValue = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getName().equals(getIdFieldName())) {
                    statement.setObject(index++, field.get(t));
                } else {
                    idValue = (int) field.get(t);
                }
            }
            statement.setInt(index, idValue);
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            LOGGER.log(Level.WARNING, type.getName() + ": update " + ex.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Delete.
     *
     * @param id the id
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + type.getSimpleName().toLowerCase() + " WHERE " + getIdFieldName() + " = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: delete " + ex.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}