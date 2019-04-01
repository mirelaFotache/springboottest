package com.pentalog.bookstore.dto;

import lombok.Data;

@Data
public class RoleDTO {

    private int id;
    private String name;

    /** Getter/setter section */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
