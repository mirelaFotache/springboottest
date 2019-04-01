package com.pentalog.bookstore.services;

import com.pentalog.bookstore.exception.BookstoreException;
import com.pentalog.bookstore.persistence.entities.Cart;
import com.pentalog.bookstore.persistence.repositories.CartJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

@Service
@Transactional
public class CartService {

    @Resource
    private CartJpaRepository cartJpaRepository;

    /**
     * find cart by title
     * @param title title
     * @return cart by title
     */
    public Collection<Cart> findByTitle(String title){
        return cartJpaRepository.findByTitle(title.toLowerCase());
    }

    /**
     * Find cart by id
     * @param id id
     * @return cart by id
     */
    public Cart findById(Integer id) {
        return cartJpaRepository.findById(id).orElse(null);
    }
    /**
     * Find carts
     * @return all carts
     */
    public Collection<Cart> findAll() {
        return cartJpaRepository.findAll();
    }


    /**
     * Save cart
     * @param cart cart
     * @return persisted cart
     */
    public Cart insert(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    /**
     * Update cart
     * @param cart cart
     * @return updated cart
     */
    public Cart update(Integer id, Cart cart) {
        Cart savedCart = null;
        Cart persistedCart = cartJpaRepository.findById(id).orElse(null);

        if(persistedCart!=null && cart!=null) {
            persistedCart.setTitle(cart.getTitle());
            persistedCart.setPrice(cart.getPrice());
            persistedCart.setQuantity(cart.getQuantity());

            savedCart = cartJpaRepository.save(persistedCart);
        }else{
            throw new BookstoreException("Cart not found!");
        }
        return savedCart;
    }

    /**
     * Delete cart by id
     * @param id id
     * @return return 0 when user not removed and 1 if user removed successfully
     */
    public Long delete(Integer id) {
        return cartJpaRepository.removeById(id);
    }
}
