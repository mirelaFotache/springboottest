package com.pentalog.bookstore.persistence.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue
    @Column
    private int id;
    @Version
    private Integer version;

    /**
     * Define primary key constraints
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customerCarts", fetch = FetchType.LAZY)
    private List<Cart> customers;

    /**
     * Getter/setter section
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Cart> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Cart> customers) {
        this.customers = customers;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
