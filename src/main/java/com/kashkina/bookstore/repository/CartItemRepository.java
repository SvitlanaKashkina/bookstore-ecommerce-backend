package com.kashkina.bookstore.repository;

import com.kashkina.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // find all items in the cart
    List<CartItem> findByCartId(Long cartId);

    // delete all items in the cart
    void deleteByCartId(Long cartId);

    // check if this item is already in the cart
    boolean existsByCartIdAndBookId(Long cartId, Long bookId);
}
