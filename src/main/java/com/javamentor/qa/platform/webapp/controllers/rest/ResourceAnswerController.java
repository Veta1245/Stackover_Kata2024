package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Tag(name = "ResourceAnswerController", description = "Контроллер для работы с ответами на вопросы")
@Slf4j
@RequestMapping("/api/user/question/{questionId}/answer")
@AllArgsConstructor
@RestController
public class ResourceAnswerController {
    private final VoteAnswerService voteAnswerService;
    private final AnswerService answerService;
    private final AnswerDtoService answerDtoService;


    //    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @Operation(summary = "Получение и пометка ответа на удаление по Id",
            description = "Вытаскивает из базы ответ присваивает  полям is_deleted и is_deleted_by_moderator значения true и сохраняет"
    )
    @DeleteMapping(value = "/{answerId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "404", description = "Answer not found"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "301", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    public ResponseEntity<String> markAnswerToDelete(@PathVariable("answerId") Long answerId) {
        try {
            Answer answer = answerService.getAndMarkForDeleteAnswer(answerId);
            if (answer != null) {
                return new ResponseEntity<>("Answer marked for deletion successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Answer with id " + answerId + " not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while marking answer for deletion", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Operation(summary = "Получение вопроса по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Answer not found"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "301", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")})
    @PutMapping(value = "/{answerId}/body")
    public ResponseEntity<AnswerDto> updateAnswerBody(@PathVariable("answerId") Long answerId, @RequestBody AnswerDto answerDto) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("The user has been successfully received");

        try {
            return answerDtoService.updateAnswer(answerDto, answerId, user)
                    .map(answerDtoMap -> {
                        log.info("Answer DTO found with questionId and userId: {}, {}", answerDtoMap.getQuestionId(), answerDtoMap.getUserId());
                        return new ResponseEntity<>(answerDtoMap, HttpStatus.OK);
                    })
                    .orElseGet(() -> {
                        log.warn("Answer DTO not found for ID: {}", answerDto.getId());
                        return new ResponseEntity<>(new AnswerDto(), HttpStatus.NOT_FOUND);
                    });
        } catch (NotFoundException e ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
                                               @AuthenticationPrincipal User user) {
        try {
            Optional<Answer> answer = Optional.ofNullable(answerService.getByAnswerIdWithoutUser(answerId, user));
            if (user == null) {
                log.info("Пользователь не найден");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (answer.isEmpty()) {
                log.info("Ответ не найден");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Long votesCount = voteAnswerService.downVoteAnswer(answerId, user);
                log.info("Успешно отправлен отрицательный голос на ответ с id {}", answerId);
                return new ResponseEntity<>(votesCount, HttpStatus.OK);
            }
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
            @AuthenticationPrincipal User user) {
        try {

            Long votesCount = voteAnswerService.voteUpToAnswer(answerId, user);
            log.info("Отправка положительного голоса прошла успешно. ID вопроса: {}", answerId);
            return new ResponseEntity<>(votesCount, HttpStatus.OK);
        } catch (Exception e) {
            log.error("При попытке голосования за ответ c ID {} произошла ошибка", answerId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Получить все ответы по id вопроса",
            description = "Получить все ответы по id вопроса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ответы получены"),
            @ApiResponse(responseCode = "401", description = "Необходима авторизация"),
            @ApiResponse(responseCode = "404", description = "Вопрос не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при выполнении запроса")
    })
    @GetMapping
    public ResponseEntity<Optional<AnswerDto>> getAllAnswer(
            @Parameter(description = "id вопроса для получения ответов")
            @PathVariable("questionId") Long questionId,
            @AuthenticationPrincipal User user) {
        try {
            long tempID = 1L;
            // todo: удалить заглушку
            Long userId = (user != null) ? user.getId() : tempID;
            Optional<AnswerDto> answers = answerDtoService.getAllAnswersDtoByQuestionId(questionId,
                    userId);
            if (answers.isEmpty()) {
                log.warn("Ответы для вопроса с ID {} не найдены", questionId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            log.info("Получение всех ответов по id вопроса: {}", questionId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            log.error("При попытке получения ответа c ID {} произошла ошибка", questionId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}





