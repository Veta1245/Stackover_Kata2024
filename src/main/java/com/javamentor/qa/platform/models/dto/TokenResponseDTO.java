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
@Schema(description = "токен")
public class TokenResponseDTO {
    @NotBlank
    @Schema(description = "роль пользователя")
    private String role;
    @NotBlank
    @Schema(description = "сгенерированный токен")
    private String token;
}