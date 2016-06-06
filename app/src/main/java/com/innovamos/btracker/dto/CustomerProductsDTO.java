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
    private String price;
    private String discount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFinalPrice() {
        Integer finalPrice = 0;

        if (this.price != null) {
            if (this.discount != null) {
                finalPrice = (int) Math.ceil(
                        Double.parseDouble(this.price) - Double.parseDouble(this.price) * Double.parseDouble(this.discount) / 100);
            }
            else {
                finalPrice = (int) Math.ceil(Double.parseDouble(this.price));
            }
        }

        return finalPrice;
    }
}
