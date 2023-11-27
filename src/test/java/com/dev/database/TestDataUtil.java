package com.dev.database;

import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.dto.BookDto;
import com.dev.database.domain.model.Author;
import com.dev.database.domain.model.Book;

public final class TestDataUtil {
    private TestDataUtil(){}

    public static Author createTestAuthor(){
        return Author.builder()
                .id(1L)
                .name("Khaled")
                .age(80)
                .build();
    }
    public static Author createTestAuthorB(){
        return Author.builder()
                .id(2L)
                .name("Mohamed")
                .age(30)
                .build();
    }

    public static AuthorDto createTestAuthorDtoB(){
        return AuthorDto.builder()
                .id(2L)
                .name("Mohamed")
                .age(30)
                .build();
    }
    public static Author createTestAuthorC(){
        return Author.builder()
                .id(3L)
                .name("Omar")
                .age(40)
                .build();
    }

    public static AuthorDto createTestAuthorDto(){
        return AuthorDto.builder()
                .id(1L)
                .name("Khaled")
                .age(80)
                .build();
    }

    public static Book createTestBook(final Author author){
        return Book.builder()
                .isbn("978-1-2345-6789-0")
                .title("The Shadow")
                .author(author)
                .build();
    }
    public static BookDto createTestBookDto(final AuthorDto author){
        return BookDto.builder()
                .isbn("978-1-2345-6789-1")
                .title("The Shadow")
                .author(author)
                .build();
    }
    public static Book createTestBookB(final Author author){
        return Book.builder()
                .isbn("978-1-2345-6789-1")
                .title("The Shadow2")
                .author(author)
                .build();
    }
    public static BookDto createTestBookDtoB(final AuthorDto authorDto){
        return BookDto.builder()
                .isbn("978-1-2345-6789-1")
                .title("The Darkness")
                .author(authorDto)
                .build();
    }
    public static Book createTestBookC(final Author author){
        return Book.builder()
                .isbn("978-1-2345-6789-2")
                .title("The Shadow3")
                .author(author)
                .build();
    }

}
