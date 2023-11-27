package com.dev.database.controllers;

import com.dev.database.TestDataUtil;
import com.dev.database.domain.dto.BookDto;
import com.dev.database.domain.model.Book;
import com.dev.database.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private BookService bookService;
    @Autowired
    public BookControllerIntegrationTests(BookService bookService,MockMvc mockMvc){
        this.mockMvc=mockMvc;
        this.objectMapper=new ObjectMapper();
        this.bookService=bookService;
    }

    @Test
    public void testThatCreateBooksReturnsHttpStatus201Created() throws Exception{
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson=objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

    }

    @Test
    public void testThatCreateBooksReturnsCreatedBook() throws Exception{
        BookDto bookDto = TestDataUtil.createTestBookDto(null);
        String createBookJson=objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+bookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );

    }
    @Test
    public void testThatGetBookReturnsHttpStatus200WhenBookExist() throws Exception{

        Book testBook= TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(testBook.getIsbn(),testBook);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatGetBookReturnsHttpStatus404WhenBookDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
    @Test
    public void testThatGetAuthorReturnsAuthor() throws Exception{
        Book testBook= TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(testBook.getIsbn(),testBook);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+testBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBook.getTitle())
        );
    }


    @Test
    public void testThatGetAllBooksReturnsHttpStatus200() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAllBooksReturnsListOfBooks() throws Exception{
        Book testBook=TestDataUtil.createTestBook(null);
        bookService.createUpdateBook(testBook.getIsbn(),testBook);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(testBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value(testBook.getTitle())
        );
    }

    @Test
    public void testThatUpdateBookReturnsHttpStatus200WhenBookExists() throws Exception{
        Book testBook=TestDataUtil.createTestBook(null);
        Book savedBook = bookService.createUpdateBook(testBook.getIsbn(),testBook);

        BookDto testBookDto=TestDataUtil.createTestBookDtoB(null);
        String bookDtoJson= objectMapper.writeValueAsString(testBookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }


    @Test
    public void testThatUpdateBookUpdatesExistingBook() throws Exception{
        Book testBook=TestDataUtil.createTestBook(null);
        Book savedBook = bookService.createUpdateBook(testBook.getIsbn(),testBook);

        BookDto testBookDto=TestDataUtil.createTestBookDtoB(null);
        String bookDtoJson= objectMapper.writeValueAsString(testBookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDto.getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateExistingBookReturnsHttpStatus200Ok() throws Exception {
        Book testBook= TestDataUtil.createTestBook(null);
        Book savedBook = bookService.createUpdateBook(testBook.getIsbn(),testBook);

        BookDto testBookDto=TestDataUtil.createTestBookDto(null);
        testBookDto.setTitle("UPDATED");
        String bookDtoJson= objectMapper.writeValueAsString(testBookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateExistingBookReturnsUpdatedBook() throws Exception {
        Book testBook= TestDataUtil.createTestBook(null);
        Book savedBook = bookService.createUpdateBook(testBook.getIsbn(),testBook);

        BookDto testBookDto=TestDataUtil.createTestBookDto(null);
        testBookDto.setTitle("UPDATED");
        String bookDtoJson= objectMapper.writeValueAsString(testBookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDto.getTitle())
        );
    }
}
