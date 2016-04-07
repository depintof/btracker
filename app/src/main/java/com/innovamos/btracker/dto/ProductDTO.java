package com.innovamos.btracker.dto;

/**
 * DTO for Product
 */
public class ProductDTO {

    /*
    Columnas
    */
    private String id;
    private String name;
    private String description;
    private String localUri;

    private String price;
    private String discount;
    private String created;
    private String modified;

    public ProductDTO(String id, String name, String description, String localUri, String price, String discount, String created, String modified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.localUri = localUri;
        this.price = price;
        this.discount = discount;
        this.created = created;
        this.modified = modified;
    }

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

    public String getLocalUri() {
        return localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
