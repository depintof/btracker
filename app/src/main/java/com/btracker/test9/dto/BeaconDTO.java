package com.btracker.test9.dto;

/**
 * DTO for BeaconDTO
 */
public class BeaconDTO {
    /*
    Columnas
    */
    private String id;
    private String uuid;
    private String major;
    private String minor;
    private String detectionRange;
    private String created;
    private String modified;

    public BeaconDTO(String idBeacon, String uuid, String major, String minor, String detectionRange, String created, String modified) {
        this.id = idBeacon;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.detectionRange = detectionRange;
        this.created = created;
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getDetectionRange() {
        return detectionRange;
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
    public boolean compararCon(BeaconDTO beaconDTO) {
        return this.uuid.compareTo(beaconDTO.uuid) == 0 &&
                this.major.compareTo(beaconDTO.major) == 0 &&
                this.minor.compareTo(beaconDTO.minor) == 0;
    }
}
