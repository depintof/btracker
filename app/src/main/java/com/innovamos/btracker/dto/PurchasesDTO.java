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
    private String name;
    private String discount;
    private String description;

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
}
