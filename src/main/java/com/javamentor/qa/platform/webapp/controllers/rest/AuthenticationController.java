package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;
import com.javamentor.qa.platform.security.jwt.service.JwtAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Tag(name = "AuthenticationController", description = "Контроллер с аутентификацией пользователя, через получение JWT токена")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final JwtAuthService jwtAuthService;

    @Operation(summary = "Принимает запрос на аутентификацию пользователя и возвращает токен доступа в ответ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь аутентифицирован и токен сгенерирован"),
            @ApiResponse(responseCode = "400", description = "Что-то не так с логином или паролем"),
            @ApiResponse(responseCode = "404", description = "Что-то пошло не так, страница не найдена"),
            @ApiResponse(responseCode = "500", description = "При выполнении запроса произошла ошибка")
    })
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> authentication(@RequestBody
                                                           @Parameter(description = "логин и пароль пользователя из тела запроса")
                                                           AuthenticationRequestDTO authenticationRequest) {
        log.info("Аутентификация пользователя на основе предоставленных логина и пароля");
        return jwtAuthService.authenticateUser(authenticationRequest)
                .map(tokenResponseDTO -> {
                    log.info("Создание объекта TokenResponseDTO с токеном и статусом OK");
                    return new ResponseEntity<>(tokenResponseDTO, HttpStatus.OK);})
                .orElseGet(() ->{
                    log.info("Аутентификация не удалась, возвращается пустой объект TokenResponseDTO и статус BAD_REQUEST");
                    return new ResponseEntity<>(new TokenResponseDTO(), HttpStatus.BAD_REQUEST);
        });

    }

    @PostMapping("/logout")
    @Operation(summary = "выход пользователя из системы")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        /**
         * TODO: логика выхода пользователя из системы, пока что просто шаблон
         */
    }

}
