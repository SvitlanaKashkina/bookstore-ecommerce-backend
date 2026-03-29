package com.kashkina.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kashkina.bookstore.entity.Book;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Search by category
    List<Book> findByCategoryIgnoreCase(String category);

    // Search by name with partial match
    List<Book> findByTitleContainingIgnoreCase(String title);
}
