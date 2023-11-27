package com.dev.database.services;

import com.dev.database.domain.model.Book;
import com.dev.database.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }
    public Book createUpdateBook(String isbn,Book book) {
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> findAllPageable(Pageable pageable){return bookRepository.findAll(pageable); }
    public Optional<Book> findById(String isbn) {
        return bookRepository.findById(isbn);
    }


    public void deleteById(String isbn) {
        bookRepository.deleteById(isbn);
    }


    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    public Book partialUpdate(String isbn, Book book) {
        book.setIsbn(isbn);
        return bookRepository.findById(isbn).map(existingBook -> {
            Optional.ofNullable(book.getTitle()).ifPresent(existingBook::setTitle);
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

}
