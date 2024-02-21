package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.converters.QuestionConverter;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/user/question")
public class ResourceQuestionController {

    private final QuestionService questionService;
    private final TagService tagService;
    private final UserService userService;
    private final QuestionConverter questionConverter;

    @PostMapping
    @Operation(summary = "Метод для добавления нового вопроса и возвращения QuestionDto ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вопрос успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Вопрос не добавлен")
    })
    public ResponseEntity<QuestionDto> addNewQuestion(@RequestBody QuestionCreateDto questionCreateDto) {
        log.info("Запрос на создние нового вопроса");

        //Тут должно быть получение юзера из спринг секьюрити, но пока что используется заглушка
        //TODO: Исправить после реализации Spring Security

        User user = userService.getByEmail("user@yandex.ru").orElseThrow(() -> new RuntimeException("User не найден"));
        log.warn("пользователь берется из БД с email = user@yandex.ru");

        log.info("Запрос успешно выполнен");
        return new ResponseEntity<>(questionConverter.questionToQuestionDto(questionService.addNewQuestion(questionCreateDto,
                tagService.addTag(questionCreateDto), user)), HttpStatus.CREATED);
    }

}
