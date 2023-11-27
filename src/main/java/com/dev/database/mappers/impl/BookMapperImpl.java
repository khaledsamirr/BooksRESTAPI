package com.dev.database.mappers.impl;

import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.dto.BookDto;
import com.dev.database.domain.model.Author;
import com.dev.database.domain.model.Book;
import com.dev.database.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapperImpl implements Mapper<Book, BookDto> {

    private ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }
    @Override
    public BookDto mapTo(Book book) {
        return modelMapper.map(book,BookDto.class);
    }

    @Override
    public Book mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto,Book.class);
    }
}
