package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.JmApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResourceUserControllerTest extends JmApplicationTests {

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceUserController/01_roles.yml",
            "webapp/controllers/rest/ResourceUserController/02_users.yml",
            "webapp/controllers/rest/ResourceUserController/03_question.yml",
            "webapp/controllers/rest/ResourceUserController/04_answer.yml",
            "webapp/controllers/rest/ResourceUserController/05_reputation.yml",
            "webapp/controllers/rest/ResourceUserController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceUserController/07_votes_on_questions.yml",
    })
    void userFoundByIDReturns200() throws Exception {
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(get("/api/user/101")
                                .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceUserController/01_roles.yml",
            "webapp/controllers/rest/ResourceUserController/02_users.yml",
            "webapp/controllers/rest/ResourceUserController/03_question.yml",
            "webapp/controllers/rest/ResourceUserController/04_answer.yml",
            "webapp/controllers/rest/ResourceUserController/05_reputation.yml",
            "webapp/controllers/rest/ResourceUserController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceUserController/07_votes_on_questions.yml",
    })
    void userFoundByIDReturnsUserData() throws Exception {
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(get("/api/user/101")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.email").value("admin@mail.com"))
                .andExpect(jsonPath("$.fullName").value("Admin1"))
                .andExpect(jsonPath("$.reputation").value(8))
                .andExpect(jsonPath("$.votes").value(3));
    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceUserController/01_roles.yml",
            "webapp/controllers/rest/ResourceUserController/02_users.yml",
            "webapp/controllers/rest/ResourceUserController/03_question.yml",
            "webapp/controllers/rest/ResourceUserController/04_answer.yml",
            "webapp/controllers/rest/ResourceUserController/05_reputation.yml",
            "webapp/controllers/rest/ResourceUserController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceUserController/07_votes_on_questions.yml",
    })
    void userNotFoundByIDReturns404() throws Exception {
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(get("/api/user/201")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceUserController/01_roles.yml",
            "webapp/controllers/rest/ResourceUserController/02_users.yml",
            "webapp/controllers/rest/ResourceUserController/03_question.yml",
            "webapp/controllers/rest/ResourceUserController/04_answer.yml",
            "webapp/controllers/rest/ResourceUserController/05_reputation.yml",
            "webapp/controllers/rest/ResourceUserController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceUserController/07_votes_on_questions.yml",
    })
    void withoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/user/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}