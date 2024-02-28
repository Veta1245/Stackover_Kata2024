package com.javamentor.qa.platform.webapp.controllers.rest;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.JmApplicationTests;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class ResourseAnswerControllerTest extends JmApplicationTests {


    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceAnswerController/01_roles.yml",
            "webapp/controllers/rest/ResourceAnswerController/02_users.yml",
            "webapp/controllers/rest/ResourceAnswerController/03_question.yml",
            "webapp/controllers/rest/ResourceAnswerController/04_answer.yml",
            "webapp/controllers/rest/ResourceAnswerController/05_reputation.yml",
            "webapp/controllers/rest/ResourceAnswerController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceAnswerController/07_votes_on_questions.yml",
    })
    public void AnswerUpdateWithoutBody() throws Exception {
        String token = "Bearer "+obtainAccessToken("admin@mail.com", "pass");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/1/answer/101/body")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isForbidden());

    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceAnswerController/01_roles.yml",
            "webapp/controllers/rest/ResourceAnswerController/02_users.yml",
            "webapp/controllers/rest/ResourceAnswerController/03_question.yml",
            "webapp/controllers/rest/ResourceAnswerController/04_answer.yml",
            "webapp/controllers/rest/ResourceAnswerController/05_reputation.yml",
            "webapp/controllers/rest/ResourceAnswerController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceAnswerController/07_votes_on_questions.yml",
    })
    public void AnswerUpdateWithInvalidData() throws Exception {

        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(104L);
        answerDto.setUserId(102L);
        answerDto.setQuestionId(102L);
        answerDto.setBody("new body");
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/102/answer/102/body")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceAnswerController/01_roles.yml",
            "webapp/controllers/rest/ResourceAnswerController/02_users.yml",
            "webapp/controllers/rest/ResourceAnswerController/03_question.yml",
            "webapp/controllers/rest/ResourceAnswerController/04_answer.yml",
            "webapp/controllers/rest/ResourceAnswerController/05_reputation.yml",
            "webapp/controllers/rest/ResourceAnswerController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceAnswerController/07_votes_on_questions.yml",
    })
    public void AnswerUpdateWithCorrectData() throws Exception {

        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(104L);
        answerDto.setUserId(102L);
        answerDto.setQuestionId(102L);
        answerDto.setBody("new body");
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/102/answer/104/body")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDto))
                        .accept(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("new body"));

    }
    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceAnswerController/01_roles.yml",
            "webapp/controllers/rest/ResourceAnswerController/02_users.yml",
            "webapp/controllers/rest/ResourceAnswerController/03_question.yml",
            "webapp/controllers/rest/ResourceAnswerController/04_answer.yml",
            "webapp/controllers/rest/ResourceAnswerController/05_reputation.yml",
            "webapp/controllers/rest/ResourceAnswerController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceAnswerController/07_votes_on_questions.yml",
    })
    public void AnswerUpdateWithEmptyBody() throws Exception {

        AnswerDto answerDto = new AnswerDto();
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/102/answer/104/body")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceAnswerController/01_roles.yml",
            "webapp/controllers/rest/ResourceAnswerController/02_users.yml",
            "webapp/controllers/rest/ResourceAnswerController/03_question.yml",
            "webapp/controllers/rest/ResourceAnswerController/04_answer.yml",
            "webapp/controllers/rest/ResourceAnswerController/05_reputation.yml",
            "webapp/controllers/rest/ResourceAnswerController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceAnswerController/07_votes_on_questions.yml",
    })
    public void AnswerUpdateIsDeleted() throws Exception {

        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(105L);
        answerDto.setUserId(102L);
        answerDto.setQuestionId(102L);
        answerDto.setBody("new body");
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/102/answer/105/body")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDto)))
                .andExpect(status().isForbidden());

    }

    @Test
    @DataSet(value = {"webapp/controllers/rest/ResourceAnswerController/01_roles.yml",
            "webapp/controllers/rest/ResourceAnswerController/02_users.yml",
            "webapp/controllers/rest/ResourceAnswerController/03_question.yml",
            "webapp/controllers/rest/ResourceAnswerController/04_answer.yml",
            "webapp/controllers/rest/ResourceAnswerController/05_reputation.yml",
            "webapp/controllers/rest/ResourceAnswerController/06_votes_on_answers.yml",
            "webapp/controllers/rest/ResourceAnswerController/07_votes_on_questions.yml",
    })
    public void AnswerUpdateAnswerNotExist() throws Exception {

        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(1000000000L);
        answerDto.setUserId(102L);
        answerDto.setQuestionId(102L);
        answerDto.setBody("new body");
        String token = "Bearer "+obtainAccessToken("user1@mail.com", "pass");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/question/102/answer/1000000000/body")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerDto)))
                .andExpect(status().isNotFound());

    }

}
