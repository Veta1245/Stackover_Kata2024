package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "ResourceTagController", description = "Контроллер для работы с тэгами")
@RestController
@RequestMapping("/api/user/tag")
@Slf4j
@AllArgsConstructor
public class ResourceTagController {

    private final TagDtoService tagDtoService;
    private final TrackedTagService trackedTagService;

    @Operation(summary = "метод для отображения топ 3 тэгов пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/top-3tags")
    public ResponseEntity<List<TagDto>> getTop3TagsUser(@AuthenticationPrincipal User user) {
        log.info("Топ 3 тэгов пользователя с ID {}", user.getId());
        return new ResponseEntity<>(tagDtoService.getTop3TagsByUserId(user.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Добавление тэга в отслеживаемые")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тег успешно добавлен в отслеживаемые"),
            @ApiResponse(responseCode = "400", description = "Запрос неверен"),
            @ApiResponse(responseCode = "409", description = "Может быть связано с тем, что данный тег уже добавлен в список отслеживаемых"),
            @ApiResponse(responseCode = "401", description = "Вы не авторизованы, нет доступа"),
            @ApiResponse(responseCode = "500", description = "При выполнении запроса произошла ошибка")
    })
    @PostMapping("/{tagId}/tracked")
    public ResponseEntity<TagDto> addTagToTracked(@PathVariable Long tagId,
                                                  @AuthenticationPrincipal User user) {
        try {
            Optional<TagDto> tagDto = tagDtoService.getTag(tagId);
            if (user == null) {
                log.info("Пользователь не найден");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (tagDto.isEmpty()) {
                log.info("Тэг не найден");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                log.info("Тэг с id {}, пользователя с id {}, добавлен в отслеживаемые", tagId, user.getId());
                return new ResponseEntity<>(trackedTagService.addTagToTracked(user, tagId), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("При попытке добавить тэг с id {}, произошла ошибка", tagId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Метод возвращает топ 10 тегов",
            description = "Возвращает топ 10 тегов используя таблицу вопросов и связь этой таблицы с тегами" +
                          "просто суммирует частоту тегов встречающихся в вопросах ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "301", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "400", description = "Неправильный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/related")
    public ResponseEntity<Optional<List<RelatedTagDto>>> getTop10Tags() {

        Optional<List<RelatedTagDto>> topTags = tagDtoService.getTop10Tags();
        if (topTags.isEmpty()) {
            log.info("Теги не найдены :(");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("Top 10 tags: {}", topTags);
            return new ResponseEntity<>(topTags, HttpStatus.OK);
        }
    }
}

