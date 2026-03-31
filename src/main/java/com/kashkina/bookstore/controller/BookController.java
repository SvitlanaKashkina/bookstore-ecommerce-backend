package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.BookDTO;
import com.kashkina.bookstore.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("HTTP DELETE /books/{} - Delete book", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // Search for books by title (partial match)
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDTO>> getBooksByTitle(@RequestParam("q") String title) {
        log.info("HTTP GET /books/search/title?q={} - Search books by title", title);
        List<BookDTO> books = bookService.getBooksByTitle(title);
        log.info("Found {} books with title containing '{}'", books.size(), title);
        return ResponseEntity.ok(books);
    }

    // Search for books by category
    @GetMapping("/search/category")
    public ResponseEntity<List<BookDTO>> getBooksByCategory(@RequestParam("q") String category) {
        log.info("HTTP GET /books/search/category?q={} - Search books by category", category);
        List<BookDTO> books = bookService.getBooksByCategory(category);
        log.info("Found {} books in category '{}'", books.size(), category);
        return ResponseEntity.ok(books);
    }
}
