package com.innovamos.btracker.dto;

/**
 * DTO for CustomerProductsDTO
 */
public class CustomerProductsDTO {

    /*
        Columnas
     */
    private String customer_id;
    private String product_id;
    private String mac;
    private String name;
    private double price;
    private double discount;
    private String description;

    public CustomerProductsDTO(String idCustomer, String idProduct) {
        this.customer_id = idCustomer;
        this.product_id = idProduct;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
