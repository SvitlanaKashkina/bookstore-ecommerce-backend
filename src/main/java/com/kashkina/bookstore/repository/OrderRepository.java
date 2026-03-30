package com.kashkina.bookstore.repository;

import com.kashkina.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
