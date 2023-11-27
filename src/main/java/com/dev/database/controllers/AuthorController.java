package com.dev.database.controllers;

import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.model.Author;
import com.dev.database.mappers.Mapper;
import com.dev.database.services.AuthorService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private AuthorService authorService;

    Mapper<Author,AuthorDto> authorMapper;
    public AuthorController(AuthorService authorService,    Mapper<Author,AuthorDto> authorMapper){
        this.authorService=authorService;
        this.authorMapper=authorMapper;
    }
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto){
        Author author=authorMapper.mapFrom(authorDto);
        Author savedAuthor=authorService.create(author);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthor),HttpStatus.CREATED);
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors(){
        List<Author> authors=authorService.findAll();
        return authors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthor( @PathVariable("id") Long id){
        Optional<Author> author=authorService.findById(id);
        return author.map(authorEntity->{
            AuthorDto authorDto=authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto,HttpStatus.OK);
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );

    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto){
        if(!authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        authorDto.setId(id);
        Author author = authorMapper.mapFrom(authorDto);
        Author savedAuthor = authorService.create(author);

        return new ResponseEntity<>(authorMapper.mapTo(savedAuthor),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDto> partialUpdate( @PathVariable("id") Long id, @RequestBody AuthorDto authorDto){
        if(!authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Author author=authorMapper.mapFrom(authorDto);
        Author updatedAuthor=authorService.partialUpdate(id,author);
        return new ResponseEntity<>(
                authorMapper.mapTo(updatedAuthor),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable("id") Long id){
        if(!authorService.isExists(id)){
            return new ResponseEntity<>("Author is Not Found!",HttpStatus.NOT_FOUND);
        }
        authorService.deleteById(id);
        return new ResponseEntity<>("Author is successfully deleted!",HttpStatus.OK);
    }

}
