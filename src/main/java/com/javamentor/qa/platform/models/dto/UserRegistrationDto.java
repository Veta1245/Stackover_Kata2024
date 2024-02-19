package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.NotEmpty;

@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "данные регистрации пользователя")
public class UserRegistrationDto {
    @NotEmpty
    @Schema(description = "имя пользователя")
    private String firstName;

    @NotEmpty
    @Schema(description = "фамилия пользователя")
    private String lastName;

    @NotEmpty
    @Schema(description = "почта пользователя")
    private String email;

    @NotEmpty
    @Schema(description = "пароль пользователя")
    private String password;
}
