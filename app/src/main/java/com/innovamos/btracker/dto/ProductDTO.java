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
    private String price;
    private String discount;
    private String terms;
    private String picture;
    private String picture_dir;
    private String status;

    private String created;
    private String modified;
    private String type;

    public ProductDTO(String id, String name, String description, String price, String discount, String terms, String picture, String pictureDir, String status, String created, String modified, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.terms = terms;
        this.picture = picture;
        this.picture_dir = pictureDir;
        this.status = status;
        this.created = created;
        this.modified = modified;
        this.type = type;
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

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture_dir() {
        return picture_dir;
    }

    public void setPicture_dir(String picture_dir) {
        this.picture_dir = picture_dir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
