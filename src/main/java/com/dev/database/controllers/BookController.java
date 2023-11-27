package com.dev.database.controllers;

import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.dto.BookDto;
import com.dev.database.domain.model.Author;
import com.dev.database.domain.model.Book;
import com.dev.database.mappers.Mapper;
import com.dev.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;
    Mapper<Book, BookDto> bookMapper;

    public BookController(BookService bookService,Mapper<Book, BookDto> bookMapper){
        this.bookService=bookService;
        this.bookMapper=bookMapper;
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@RequestBody BookDto bookDto, @PathVariable("isbn") String isbn){
        Book book=bookMapper.mapFrom(bookDto);
        boolean bookExists= bookService.isExists(isbn);
        Book savedUpdatedBook=bookService.createUpdateBook(isbn,book);
        BookDto savedUpdatedBookDto=bookMapper.mapTo(savedUpdatedBook);

        if(bookExists){
            return new ResponseEntity<>(savedUpdatedBookDto,HttpStatus.OK);
        }else{

            return new ResponseEntity<>(savedUpdatedBookDto,HttpStatus.CREATED);
        }
    }

    @GetMapping
    public Page<BookDto> getAllBooks(Pageable pageable){
        Page<Book> books=bookService.findAllPageable(pageable);
        return books.map(bookMapper::mapTo);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn){
        Optional<Book> book=bookService.findById(isbn);
        return book.map(bookEntity->{
            BookDto bookDto=bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(bookDto,HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PatchMapping("/{isbn}")
    public ResponseEntity<BookDto> partialUpdate(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto){
        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Book book=bookMapper.mapFrom(bookDto);
        Book updatedBook=bookService.partialUpdate(isbn,book);
        return new ResponseEntity<>(
                bookMapper.mapTo(updatedBook),
                HttpStatus.OK
        );


    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("isbn") String isbn){

        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>("Book is Not Found!",HttpStatus.NOT_FOUND);
        }
        bookService.deleteById(isbn);
        return new ResponseEntity<>("Book is successfully deleted!",HttpStatus.OK);
    }
}
