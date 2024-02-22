package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ResourceTagController",description ="Контроллер для работы с тэгами" )
@RestController
@RequestMapping("/api/user/tag")
@Slf4j
@AllArgsConstructor
public class ResourceTagController {

    private final TagDtoService tagDtoService;

    @Operation(summary = "метод для отображения топ 3 тэгов пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/top-3tags")
    public ResponseEntity<List<TagDto>> getTop3TagsUser (@AuthenticationPrincipal User user) {
        log.info("Топ 3 тэгов пользователя с ID {}", user.getId());
        return new ResponseEntity<>(tagDtoService.getTop3TagsByUserId(user.getId()), HttpStatus.OK);
    }
}
