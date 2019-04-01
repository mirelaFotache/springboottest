package com.pentalog.bookstore.controllers;

import com.pentalog.bookstore.dto.CartDTO;
import com.pentalog.bookstore.dto.CartMapper;
import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Cart;
import com.pentalog.bookstore.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Resource
    private CartService cartService;
    @Autowired
    private CartMapper cartMapper;

    private Logger logger = LoggerFactory.getLogger(CartController.class);

    /**
     * Get book by id
     * @param id id
     * @return book by id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CartDTO> getBook(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(cartMapper.toCartDTO(cartService.findById(id)), HttpStatus.OK);
    }
    /**
     * Find cart by title
     * @param title title
     * @return cart by title
     */
    @RequestMapping(value = "/title", method = RequestMethod.GET)
    public ResponseEntity<Collection<CartDTO>> findByTitle(@RequestParam("searchBy") String title) {
            return new ResponseEntity<>(cartMapper.toCartDTOs(cartService.findByTitle(title)), HttpStatus.OK);
    }

    /**
     * Find all carts
     * @return carts
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Collection<CartDTO>> getAllCarts() {
        return new ResponseEntity<>(cartMapper.toCartDTOs(cartService.findAll()), HttpStatus.OK);
    }

    /**
     * Save cart
     * @param cartDTO cart
     * @return persisted cart
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<CartDTO> insertCart(@RequestBody CartDTO cartDTO) {
        final Cart cart = cartMapper.toCart(cartDTO);
        return new ResponseEntity<>(cartMapper.toCartDTO(cartService.insert(cart)), HttpStatus.OK);
    }

    /**
     * Update cart
     * @param cartDTO cart
     * @return updated cart
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CartDTO> updateCart(@PathVariable("id") Integer id, @RequestBody CartDTO cartDTO) {
        final Cart cart = cartMapper.toCart(cartDTO);
        return new ResponseEntity<>(cartMapper.toCartDTO(cartService.update(id, cart)), HttpStatus.OK);
    }

    /**
     * Delete cart by id
     * @param id id
     * @return status message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCart(@PathVariable("id") Integer id) {
        final Long deleted = cartService.delete(id);
        if (deleted != null && deleted.intValue() == 1)
            return new ResponseEntity<>("Cart successfully deleted!", HttpStatus.NO_CONTENT);
        else
            throw new BookstoreException("Cart not found!");
    }
}
