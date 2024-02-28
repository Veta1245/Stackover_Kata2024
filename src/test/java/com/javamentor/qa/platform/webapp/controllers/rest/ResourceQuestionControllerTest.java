package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.JmApplicationTests;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResourceQuestionControllerTest extends JmApplicationTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    @DataSet(value = "dataset/ResourceQuestionControllerTest/addNewQuestion.yml", cleanBefore = true,cleanAfter = true)
    public void addNewQuestionWithTitleEmptyOrNull() throws Exception{
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("");
        questionCreateDto.setDescription("Test description");
        questionCreateDto.setTags(List.of(new TagDto(103L,"test name3", "test description3"),
                new TagDto(104L,"test name4", "test description4")));

        mockMvc.perform(post("/api/user/question")
                        .header("Authorization", "Bearer " + obtainAccessToken("userTest1@yandex.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "dataset/ResourceQuestionControllerTest/addNewQuestion.yml", cleanBefore = true,cleanAfter = true)
    public void addNewQuestionWithDescriptionEmptyOrNull() throws Exception{
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Test title");
        questionCreateDto.setDescription("");
        questionCreateDto.setTags(List.of(new TagDto(103L,"test name3", "test description3"),
                new TagDto(104L,"test name4", "test description4")));

        mockMvc.perform(post("/api/user/question")
                        .header("Authorization", "Bearer " + obtainAccessToken("userTest1@yandex.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "dataset/ResourceQuestionControllerTest/addNewQuestion.yml", cleanBefore = true,cleanAfter = true)
    public void addNewQuestionWithTagEmptyOrNull() throws Exception{
        QuestionCreateDto questionCreateDto = new QuestionCreateDto();
        questionCreateDto.setTitle("Test title");
        questionCreateDto.setDescription("Test description");
        questionCreateDto.setTags(new ArrayList<>());

        mockMvc.perform(post("/api/user/question")
                        .header("Authorization", "Bearer " + obtainAccessToken("userTest1@yandex.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = "dataset/ResourceQuestionControllerTest/addNewQuestion.yml", cleanBefore = true,cleanAfter = true)
    public void userNotAuthorized() throws Exception {
        mockMvc.perform(get("/api/user/question"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DataSet(value = "dataset/ResourceQuestionControllerTest/addNewQuestion.yml", cleanBefore = true,cleanAfter = true)
    public void addNewQuestionWithNewTags() throws Exception{

        List<Tag> nonExistentTag = entityManager.createQuery("SELECT tag FROM Tag tag where tag.name = :tagName", Tag.class)
                .setParameter("tagName", "test name3").getResultList();
        Assertions.assertTrue(nonExistentTag.isEmpty());

        mockMvc.perform(post("/api/user/question")
                        .header("Authorization", "Bearer " + obtainAccessToken("userTest1@yandex.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new QuestionCreateDto("Test title", "Test description",
                                        List.of(new TagDto(103L,"test name3", "test description3")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.listTagDto.length()").value(1))
                .andExpect(jsonPath("$.listTagDto").exists());

        Tag excpectedTag = new Tag();
        excpectedTag.setName("test name3");
        excpectedTag.setDescription("test description3");

        Tag addedTag = entityManager.createQuery("SELECT tag FROM Tag tag where tag.name = :tagName", Tag.class)
                .setParameter("tagName", "test name3").getSingleResult();

        Assertions.assertEquals(excpectedTag.getName(), addedTag.getName());
        Assertions.assertEquals(excpectedTag.getDescription(), addedTag.getDescription());
    }

    @Test
    @DataSet(value = "dataset/ResourceQuestionControllerTest/addNewQuestion.yml", cleanBefore = true,cleanAfter = true)
    public void addNewQuestionWitExistingTags() throws Exception{

        Tag existentTag = entityManager.createQuery("SELECT tag FROM Tag tag where tag.name = :tagName", Tag.class)
                .setParameter("tagName", "TestTag1").getSingleResult();
        Assertions.assertNotNull(existentTag);

        mockMvc.perform(post("/api/user/question")
                        .header("Authorization", "Bearer " + obtainAccessToken("userTest1@yandex.ru", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new QuestionCreateDto("Test title", "Test description",
                                        List.of(new TagDto(100L,"TestTag1", "descriptionTest1")))))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.listTagDto.length()").value(1))
                .andExpect(jsonPath("$.listTagDto").exists());

        Tag addedTag = entityManager.createQuery("SELECT tag FROM Tag tag where tag.name = :tagName", Tag.class)
                .setParameter("tagName", "TestTag1").getSingleResult();

        Assertions.assertEquals(existentTag.getName(), addedTag.getName());
        Assertions.assertEquals(existentTag.getDescription(), addedTag.getDescription());


    }

}


