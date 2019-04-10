package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerDTO toCustomerDTO(Customer customer);

    List<CustomerDTO> toCustomerDTOs(List<Customer> customers);

    Customer toCustomer(CustomerDTO customerDTO);
}
