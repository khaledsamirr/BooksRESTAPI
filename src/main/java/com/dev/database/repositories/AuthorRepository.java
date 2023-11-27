package com.dev.database.repositories;

import com.dev.database.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Adding additional quires to our repository using Naming Convention
    List<Author> ageLessThan(int age);


    // Adding additional quires to our repository using Query annotation and HQL (Hibernate Query Language)
    @Query("SELECT author from Author author where author.age >?1")
    List<Author> findAuthorsWithAgeGreaterThan(int age);
}
