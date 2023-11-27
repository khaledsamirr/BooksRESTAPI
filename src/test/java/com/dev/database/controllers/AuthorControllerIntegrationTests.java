package com.dev.database.controllers;

import com.dev.database.TestDataUtil;
import com.dev.database.domain.dto.AuthorDto;
import com.dev.database.domain.model.Author;
import com.dev.database.services.AuthorService;
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
public class AuthorControllerIntegrationTests {

    private MockMvc mockMvc;

    private AuthorService authorService;

    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(AuthorService authorService,MockMvc mockMvc){
        this.mockMvc=mockMvc;
        this.objectMapper=new ObjectMapper();
        this.authorService=authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnHttp201Created() throws Exception {
        Author testAuthor = TestDataUtil.createTestAuthor();
        testAuthor.setId(null);
        String authorJson= objectMapper.writeValueAsString(testAuthor);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnSavedAuthor() throws Exception {
        Author testAuthor = TestDataUtil.createTestAuthor();
        testAuthor.setId(null);
        String authorJson= objectMapper.writeValueAsString(testAuthor);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthor.getAge())
        );
    }

    @Test
    public void testThatGetAuthorReturnsHttpStatus200WhenAuthorExist() throws Exception{

        Author testAuthor= TestDataUtil.createTestAuthor();
        authorService.create(testAuthor);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatGetAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
    @Test
    public void testThatGetAuthorReturnsAuthor() throws Exception{
        Author testAuthor=TestDataUtil.createTestAuthor();
        authorService.create(testAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+testAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthor.getAge())
        );
    }


    @Test
    public void testThatGetAllAuthorsReturnsListOfAuthors() throws Exception{
        Author testAuthor=TestDataUtil.createTestAuthor();
        authorService.create(testAuthor);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(testAuthor.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(testAuthor.getAge())
        );
    }

    @Test
    public void testThatGetAllAuthorsReturnsHttpStatus200() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus200WhenAuthorExists() throws Exception{
        Author testAuthor= TestDataUtil.createTestAuthor();
        Author saverAuthor = authorService.create(testAuthor);

        AuthorDto testAuthorDto=TestDataUtil.createTestAuthorDto();
        String authorDtoJson= objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+saverAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception{


        AuthorDto testAuthorDto=TestDataUtil.createTestAuthorDto();
        String authorDtoJson= objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdateAuthorUpdatesExistingAuthor() throws Exception{
        Author testAuthor=TestDataUtil.createTestAuthor();
        Author savedAuthor = authorService.create(testAuthor);

        AuthorDto testAuthorDto=TestDataUtil.createTestAuthorDtoB();
        String authorDtoJson= objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/"+savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDto.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200Ok() throws Exception {
        Author testAuthor= TestDataUtil.createTestAuthor();
        Author saverAuthor = authorService.create(testAuthor);

        AuthorDto testAuthorDto=TestDataUtil.createTestAuthorDto();
        testAuthorDto.setName("UPDATED");
        String authorDtoJson= objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+saverAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        Author testAuthor= TestDataUtil.createTestAuthor();
        Author saverAuthor = authorService.create(testAuthor);

        AuthorDto testAuthorDto=TestDataUtil.createTestAuthorDto();
        testAuthorDto.setName("UPDATED");
        String authorDtoJson= objectMapper.writeValueAsString(testAuthorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/"+saverAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(saverAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorDto.getAge())
        );
    }

}
