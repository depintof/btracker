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
}
