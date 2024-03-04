package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.JmApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResourceTagControllerTest extends JmApplicationTests {

    @Test
    @DataSet(value = {"dataset/ResourceTagControllerIT/user.yml",
            "dataset/ResourceTagControllerIT/question.yml",
            "dataset/ResourceTagControllerIT/tag.yml",
            "dataset/ResourceTagControllerIT/question_has_tag.yml",
    })
    void RequestSuccessRate() throws Exception {
        String token = "Bearer " + obtainAccessToken("Ivan@gmail.com", "admin");
        mockMvc.perform(get("/api/user/tag/related")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {"dataset/ResourceTagControllerIT/user.yml",
            "dataset/ResourceTagControllerIT/question.yml",
            "dataset/ResourceTagControllerIT/tag.yml",
            "dataset/ResourceTagControllerIT/question_has_tag.yml",
    })
    void testGetTopTagsSuccess() throws Exception {
        String token = "Bearer " + obtainAccessToken("Ivan@gmail.com", "admin");
        mockMvc.perform(get("/api/user/tag/related")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].id").value(106))
                .andExpect(jsonPath("$[0].title").value("JpaBuddy"))
                .andExpect(jsonPath("$[0].countQuestion").value(4))
                .andExpect(jsonPath("$[1].id").value(101))
                .andExpect(jsonPath("$[1].title").value("Hibernate"))
                .andExpect(jsonPath("$[1].countQuestion").value(3))
                .andExpect(jsonPath("$[8].countQuestion").value(1))
                .andExpect(jsonPath("$[9].countQuestion").value(1));

    }

    @Test
    @DataSet(value = {"dataset/ResourceTagControllerIT/user.yml",
            "dataset/ResourceTagControllerIT/question.yml",
            "dataset/ResourceTagControllerIT/tag.yml",
            "dataset/ResourceTagControllerIT/question_hasnt_tag.yml",
    })
    void testGetTop() throws Exception {
        String token = "Bearer " + obtainAccessToken("Ivan@gmail.com", "admin");
        mockMvc.perform(get("/api/user/tag/related")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

