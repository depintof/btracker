package com.innovamos.btracker.dto;

/**
 * DTO for Zone
 */
public class ZoneDTO {

    /*
     * Columnas
     */
    private String id;
    private String name;
    private String description;
    private String storeId;
    private String beaconId;
    private String entrance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    public ZoneDTO(String id, String name, String description, String storeId, String beaconId, String entrance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.storeId = storeId;
        this.beaconId = beaconId;
        this.entrance = entrance;
    }

}
