package com.innovamos.btracker.dto;

/**
 * DTO for PurchasesDTO
 */
public class PurchasesDTO {

    /*
           Columnas
     */
    private String id;
    private String product_id;
    private String customer_id;
    private String date;
    private String price;

    public PurchasesDTO(String id, String idProduct, String idCustomer, String date, String price) {
        this.id = id;
        this.product_id = idProduct;
        this.customer_id = idCustomer;
        this.date = date;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduct() {
        return product_id;
    }

    public void setIdProduct(String idProduct) {
        this.product_id = idProduct;
    }

    public String getIdCustomer() {
        return customer_id;
    }

    public void setIdCustomer(String idCustomer) {
        this.customer_id = idCustomer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
