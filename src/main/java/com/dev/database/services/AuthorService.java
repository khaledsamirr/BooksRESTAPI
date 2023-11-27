package com.dev.database.services;

import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.model.Author;
import com.dev.database.mappers.Mapper;
import com.dev.database.mappers.impl.AuthorMapperImpl;
import com.dev.database.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {


    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;

    }

    public Author create(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    public Author partialUpdate(Long id, Author author) {
        author.setId(id);
        return authorRepository.findById(id).map(existingAuthor -> {
            Optional.ofNullable(author.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(author.getAge()).ifPresent(existingAuthor::setAge);
            return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }
}
