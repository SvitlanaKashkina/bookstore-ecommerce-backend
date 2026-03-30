package com.kashkina.bookstore.repository;

import com.kashkina.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
