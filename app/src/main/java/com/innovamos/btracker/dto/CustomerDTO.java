package com.innovamos.btracker.dto;

/**
 * DTO for CustomerDTO
 */
public class CustomerDTO {

    /*
    Columnas
    */
    private String id;
    private String mac;
    private String created;
    private String modified;

    public CustomerDTO(String idCustomer, String mac, String created, String modified) {
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
    public boolean compararCon(CustomerDTO customerDTO) {
        return this.mac.compareTo(customerDTO.mac) == 0;
    }
}
