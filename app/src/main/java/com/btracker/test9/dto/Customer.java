package com.btracker.test9.dto;

/**
 * DTO for Customer
 */
public class Customer {

    /*
    Columnas
    */
    private String id;
    private String mac;
    private String created;
    private String modified;

    public Customer(String idCustomer, String mac, String created, String modified) {
        this.id = idCustomer;
        this.mac = mac;
        this.created = created;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public String getMac() {
        return mac;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }

    /**
     * Compara los atributos de dos beacons
     * @return true si son iguales, false si hay cambios
     */
    public boolean compararCon(Customer customer) {
        return this.mac.compareTo(customer.mac) == 0;
    }
}
