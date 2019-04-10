package com.pentalog.bookstore.persistence.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private int quantity;
    private float price;
    @Version
    private Integer version;

    /**
     * Define primary key constraints
     */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_books",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> cartBooks;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_customer_id")
    private Customer customerCarts;

    /**
     * Getter/setter section
     */

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

    public List<Book> getCartBooks() {
        return cartBooks;
    }

    public void setCartBooks(List<Book> cartBooks) {
        this.cartBooks = cartBooks;
    }

    public Customer getCustomerCarts() {
        return customerCarts;
    }

    public void setCustomerCarts(Customer customerCarts) {
        this.customerCarts = customerCarts;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
