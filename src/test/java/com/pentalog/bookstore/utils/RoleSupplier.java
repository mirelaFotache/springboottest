package com.pentalog.bookstore.utils;

import com.pentalog.bookstore.persistence.entities.Role;

public class RoleSupplier {

    public static Role supplyRoleForUpdate() {
        Role role = new Role();
        role.setId(1);
        role.setName("admin");
        return role;
    }
}
