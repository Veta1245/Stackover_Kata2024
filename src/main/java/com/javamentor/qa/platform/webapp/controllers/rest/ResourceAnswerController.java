package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@Tag(name = "ResourceAnswerController", description = "Контроллер для работы с ответами на вопросы")
@Slf4j
@RequestMapping("/api/user/question/{questionId}/answer")
@AllArgsConstructor
@RestController
public class ResourceAnswerController {
    private final VoteAnswerService voteAnswerService;
    private final UserService userService;

    @Operation(summary = "Уменьшение оценки ответа",
            description = "Голос за ответ на вопрос,уменьшает репутацию автора ответа: -5 к репутации"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оценка ответа уменьшена успешно"),
            @ApiResponse(responseCode = "400", description = "Вы не авторизованы, нет возможности проголосовать"),
            @ApiResponse(responseCode = "404", description = "Что-то пошло не так, страница не найдена"),
            @ApiResponse(responseCode = "500", description = "При выполнении запроса произошла ошибка")
    })
    @PostMapping("/{answerId}/downVote")
    public ResponseEntity<Long> downVoteAnswer(@PathVariable("answerId") Long answerId,
                                               @RequestParam("userId") Long userId) {
        try {
            //TODO: Взять юзера из секьюрити
            User user = userService.getById(userId).orElseThrow(() ->
                    new EntityNotFoundException("User not found with id: " + userId));
            Long votesCount = voteAnswerService.downVoteAnswer(answerId, user);
            log.info("Успешно отправлен отрицательный голос на ответ с id {}", answerId);
            return new ResponseEntity<>(votesCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("При попытке отправить отрицательный голос на ответ с id {}, произошла ошибка", answerId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


        }
    }


    @PostMapping("/{answerId}/upVote")
    @Operation(
            summary = "Проголосовать Up за ответ",
            description = "Увеличивает кол-во голосов на 1 и возвращает общее кол-во голосов" +
                    " Увеличивает репутацию автору на +10 очков за голос UP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно увеличено кол-во очков репутации и возвращено общее кол-во голосов"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован, принять голос нет возможности"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена, сервер не может найти страницу по запросу"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при выполнении запроса")
    })
    public ResponseEntity<Long> upVoteAnswer(
            @PathVariable("answerId") Long answerId,
            @RequestParam("userId") Long userId) {
        try {
            //TODO: взять User из Security
            User user = userService.getById(userId).orElseThrow(() ->
                    new EntityNotFoundException("User not found with id: " + userId));


            Long votesCount = voteAnswerService.voteUpToAnswer(answerId, user);
            log.info("Отправка положительного голоса прошла успешно. ID вопроса: {}", answerId);
            return new ResponseEntity<>(votesCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("При попытке голосования за ответ c ID {} произошла ошибка", answerId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

