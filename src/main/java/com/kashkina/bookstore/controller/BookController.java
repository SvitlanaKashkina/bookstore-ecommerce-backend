package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.BookDTO;
import com.kashkina.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private BookService bookService;
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    //Create a new book
    @PostMapping("/create")
    public ResponseEntity<BookDTO> createBook (@Valid @RequestBody BookDTO bookDTO) {
        log.info("HTTP POST /books - Create book");
        BookDTO created = bookService.createBook(bookDTO);
        return ResponseEntity.ok(created);
    }

    //Get all books
    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.info("HTTP GET /books - Get all books");
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        log.info("HTTP GET /books/{} - Get book by ID", id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("HTTP DELETE /books/{} - Delete book", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
