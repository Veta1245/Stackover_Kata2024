package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.security.UserDetailsServiceImpl;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Tag(name = "ResourceAnswerController", description = "Контроллер для работы с ответами на вопросы")
@Slf4j
@RequestMapping("/api/user/question/{questionId}/answer")
@AllArgsConstructor
@RestController
public class ResourceAnswerController {
    private final VoteAnswerService voteAnswerService;
    private final UserService userService;
    private final AnswerService answerService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AnswerDtoService answerDtoService;

    @Operation(summary = "Получение вопроса по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Answer not found"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "301", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PutMapping(value = "/{answerId}/body")
    public ResponseEntity<AnswerDto> updateAnswerBody(@PathVariable("answerId") Long answerId, @RequestBody AnswerDto answerDto) {

        /*
         * TODO: Исправить когда будет реализовано Security
         */
        log.warn("Uses template user from DB, NEED implement getting user from Security");
        User user = userService.getById(answerDto.getUserId()).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + answerDto.getUserId()));


        try {
            return  answerDtoService.updateAnswer(answerDto, answerId, user)
                    .map(answerDtoMap -> {
                        log.info("Answer DTO found with questionId and userId: {}, {}", answerDtoMap.getQuestionId(), answerDtoMap.getUserId());
                        return new ResponseEntity<>(answerDtoMap, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.warn("Answer DTO not found for ID: {}", answerDto.getId());
                        return new ResponseEntity<>(new AnswerDto(), HttpStatus.NOT_FOUND);
                    });
        } catch (AccessDeniedException | NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


    }

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

