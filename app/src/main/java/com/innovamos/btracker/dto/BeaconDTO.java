package com.innovamos.btracker.dto;

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
    private String detection_range;
    private String created;
    private String modified;
    private Boolean viewed;

    public BeaconDTO(String idBeacon, String uuid, String major, String minor, String detectionRange, String created, String modified) {
        this.id = idBeacon;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.detection_range = detectionRange;
        this.created = created;
        this.modified = modified;
        this.viewed = false;
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
        return detection_range;
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

    @Override
    public boolean equals(Object object){
        BeaconDTO beaconDTO;
        try {
            beaconDTO = (BeaconDTO) object;
        }
        catch (Exception e) {
            return false;
        }
        return (beaconDTO.getUuid().equalsIgnoreCase(this.uuid) && beaconDTO.getMinor().equals(this.minor) && beaconDTO.getMajor().equals(this.major));
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }
}
