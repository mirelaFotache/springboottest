package com.pentalog.bookstore.dto;

import com.pentalog.bookstore.persistence.entities.Cart;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface CartMapper {

    CartDTO toCartDTO(Cart cart);

    Collection<CartDTO> toCartDTOs(Collection<Cart> carts);

    Cart toCart(CartDTO cartDTO);

    List<Cart> toCarts(List<CartDTO> carts);
}
