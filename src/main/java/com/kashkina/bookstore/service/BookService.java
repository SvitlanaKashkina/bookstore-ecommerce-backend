package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.BookDTO;
import com.kashkina.bookstore.exception.BookNotFoundException;
import com.kashkina.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.kashkina.bookstore.entity.Book;


@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    // Saving a book
    public BookDTO createBook (BookDTO bookDTO) {
        log.info("Create book: title='{}', author='{}'", bookDTO.getTitle(), bookDTO.getAuthor());
        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .description(bookDTO.getDescription())
                .price(bookDTO.getPrice())
                .stock(bookDTO.getStock())
                .category(bookDTO.getCategory())
                .build();

        Book saved = bookRepository.save(book);
        log.info("The book was successfully saved with id={}", saved.getId());

        return mapToDTO (saved);
    }

    // Get all books
    public List<BookDTO> getAllBooks () {
        log.info("Getting all books");
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    //Get a book by ID
    public BookDTO getBookById(Long id) {
        log.info("Getting book by id");
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
        log.info("Book found: id={}, title='{}'", book.getId(), book.getTitle());
        return mapToDTO(book);
    }

    //Delete book
    public void deleteBook(Long id) {
        log.info("Delete book by id");
        if (!bookRepository.existsById(id)) {
            log.warn("Book with id={} not found, cannot be deleted", id);
            throw new BookNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
        log.info("Book with id={} successfully deleted", id);
    }

    // Search for books by title (partial match)
    public List<BookDTO> getBooksByTitle(String title) {
        log.info("Fetching books by title containing '{}'", title);
        List<BookDTO> books = bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Found {} books with title containing '{}'", books.size(), title);
        return books;
    }

    // Search books by category
    public List<BookDTO> getBooksByCategory(String category) {
        log.info("Fetching books by category='{}'", category);
        List<BookDTO> books = bookRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Found {} books in category='{}'", books.size(), category);
        return books;
    }

    // mapping Entity → DTO
    private BookDTO mapToDTO(Book book) {
        log.info("Map book to DTO");
        if (book == null) {
            throw new IllegalArgumentException("Book entity is null");
        }
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .price(book.getPrice())
                .stock(book.getStock())
                .category(book.getCategory())
                .build();
    }
}
