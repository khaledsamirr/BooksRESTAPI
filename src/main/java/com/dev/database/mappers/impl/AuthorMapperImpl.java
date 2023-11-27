package com.dev.database.mappers.impl;

import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.model.Author;
import com.dev.database.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapperImpl implements Mapper<Author, AuthorDto> {

    private ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }
    @Override
    public AuthorDto mapTo(Author author) {
        return modelMapper.map(author,AuthorDto.class);
    }

    @Override
    public Author mapFrom(AuthorDto authorDto) {
        return modelMapper.map(authorDto,Author.class);
    }
}
