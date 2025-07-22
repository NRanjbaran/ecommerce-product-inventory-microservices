package com.ecommerce.productservice.dto;

public class StockUpdateDto {

    private Integer quantity;

    // Constructors
    public StockUpdateDto() {
    }

    public StockUpdateDto(Integer quantity) {
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}