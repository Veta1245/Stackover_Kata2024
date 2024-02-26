package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.JmApplicationTests;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest extends JmApplicationTests {
    @Test
    @DataSet(value = "dataset/AuthenticationControllerTest/authentication.yml", cleanBefore = true, cleanAfter = true)
    void receivingTokenWithValidLoginAndPassword() throws Exception {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO(
                "user@gmail.com", "123456");
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.token").value(notNullValue()));
    }

    @Test
    @DataSet(value = "dataset/AuthenticationControllerTest/authentication.yml", cleanBefore = true, cleanAfter = true)
    void receivingEmptyTokenWithInvalidLogin() throws Exception {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO(
                "invalidLogin@gmail.com", "123456");
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.role").value(nullValue()))
                .andExpect(jsonPath("$.token").value(nullValue()));
    }

    @Test
    @DataSet(value = "dataset/AuthenticationControllerTest/authentication.yml", cleanBefore = true, cleanAfter = true)
    void receivingEmptyTokenWithInvalidPassword() throws Exception {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO(
                "user@gmail.com", "invalidPassword");
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.role").value(nullValue()))
                .andExpect(jsonPath("$.token").value(nullValue()));
    }

    @Test
    @DataSet(value = "dataset/AuthenticationControllerTest/authentication.yml", cleanBefore = true, cleanAfter = true)
    void receivingEmptyTokenWithEmptyLoginAndEmptyPassword() throws Exception {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO(
                "", "");
        String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.role").value(nullValue()))
                .andExpect(jsonPath("$.token").value(nullValue()));
    }
}
