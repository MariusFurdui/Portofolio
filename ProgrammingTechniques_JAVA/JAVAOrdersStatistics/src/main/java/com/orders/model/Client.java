package com.orders.model;


/**
 * The type Client.
 */
public class Client {
    private int idClient;
    private String name;
    private String email;
    private String phone;
    private String address;

    /**
     * Instantiates a new Client.
     *
     * @param idClient the id client
     * @param name     the name
     * @param email    the email
     * @param phone    the phone
     * @param address  the address
     */
    public Client(int idClient, String name, String email, String phone, String address){
        this.idClient=idClient;
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.address=address;
    }

    /**
     * Instantiates a new Client.
     */
    public Client(){
        this.idClient=0;
        this.name="";
        this.email="";
        this.phone="";
        this.address="";
    }

    /**
     * Gets id client.
     *
     * @return the id client
     */
    public int getIdClient() {
        return idClient;
    }

    /**
     * Sets id client.
     *
     * @param idClient the id client
     */
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }


}
