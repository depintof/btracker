package com.innovamos.btracker.dto;

/**
 * DTO for PurchasesDTO
 */
public class PurchasesDTO {

    /*
               Columnas
         */
    private String purchase_id;
    private String id;
    private String product_id;
    private String customer_id;
    private String date;
    private String purchase_price;
    // From Product Table
    private String name;
    private String price;
    private String discount;
    private String description;

    public PurchasesDTO(String purchase_id, String idProduct, String idCustomer, String date, String purchase_price) {
        this.purchase_id = purchase_id;
        this.product_id = idProduct;
        this.customer_id = idCustomer;
        this.date = date;
        this.purchase_price = purchase_price;
    }

    public String getId() {
        return purchase_id;
    }

    public void setId(String purchase_id) {
        this.purchase_id = purchase_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }
}
