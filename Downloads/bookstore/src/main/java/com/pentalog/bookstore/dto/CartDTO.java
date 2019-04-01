package com.pentalog.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {

    private int id;
    private String title;
    private int quantity;
    private float price;
    private List<BooksDTO> cartBooks;
    private CustomerDTO customerCarts;

    /** Getter / setter section */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<BooksDTO> getCartBooks() {
        return cartBooks;
    }

    public void setCartBooks(List<BooksDTO> cartBooks) {
        this.cartBooks = cartBooks;
    }

    public CustomerDTO getCustomerCarts() {
        return customerCarts;
    }

    public void setCustomerCarts(CustomerDTO customerCarts) {
        this.customerCarts = customerCarts;
    }
}
