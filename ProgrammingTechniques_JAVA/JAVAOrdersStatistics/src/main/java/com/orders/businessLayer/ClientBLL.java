package com.orders.businessLayer;

import com.orders.dataAccessLayer.ClientDAO;
import com.orders.model.Client;
import java.util.List;


/**
 * The type Client bll.
 */
public class ClientBLL {
    private ClientDAO clientDAO = new ClientDAO();

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Client> findAll(){
        return clientDAO.findAll();
    }

    /**
     * Find by id client.
     *
     * @param idClient the id client
     * @return the client
     */
    public Client findById(int idClient){
        return clientDAO.findById(idClient);
    }

    /**
     * Insert.
     *
     * @param client the client
     */
    public void insert(Client client){
        clientDAO.insert(client);
    }

    /**
     * Update.
     *
     * @param client the client
     */
    public void update(Client client){
        clientDAO.update(client);
    }

    /**
     * Delete.
     *
     * @param idClient the id client
     */
    public void delete(int idClient){
        clientDAO.delete(idClient);
    }

    /**
     * Find by name list.
     *
     * @param name the name
     * @return the list
     */
    public List<Client> findByName(String name) {
        return clientDAO.findAll().stream()
                .filter(c -> c.getName().equals(name))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Gets all names.
     *
     * @return the all names
     */
    public List<String> getAllNames() {
        return clientDAO.findAll().stream()
                .map(Client::getName)
                .collect(java.util.stream.Collectors.toList());
    }
}
