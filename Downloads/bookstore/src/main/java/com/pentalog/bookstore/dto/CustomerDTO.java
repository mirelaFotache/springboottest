package com.pentalog.bookstore.dto;

import lombok.Data;

@Data
public class CustomerDTO {

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
