package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.service.abstracts.system.RegistrationUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/registration")
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    private RegistrationUserService registrationUserService;

    @Operation(summary = "получить регистрационные данные и отправить письмо с кодом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "User can not be created"),
            @ApiResponse(responseCode = "200", description = "OK") } )
    @PostMapping(value = {"/", ""})
    public ResponseEntity<Void> sendMessage(@RequestBody UserRegistrationDto userRegistrationDto) {
        log.info("Request for new user registration - {}", userRegistrationDto);

        return registrationUserService.registerUser(userRegistrationDto) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
    }

    @Operation(summary = "верификация почты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad email or code"),
            @ApiResponse(responseCode = "200", description = "OK") } )
    @GetMapping(value = "/verify")
    public ResponseEntity<Void> verify(@RequestParam("email") String email, @RequestParam("code") String code) {
        log.info("Request for verify user with email {} by code {}", email, code);

        return registrationUserService.verifyEmail(email, code) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
    }
}
