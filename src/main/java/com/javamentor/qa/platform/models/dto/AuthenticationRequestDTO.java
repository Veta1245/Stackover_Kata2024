package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "запрос для аутентификации")
public class AuthenticationRequestDTO {
    @NotBlank
    @Schema(description = "логин пользователя")
    private String login;
    @NotBlank
    @Schema(description = "пароль пользователя")
    private String pass;
}