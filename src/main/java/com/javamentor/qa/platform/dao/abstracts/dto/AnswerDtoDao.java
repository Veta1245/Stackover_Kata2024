package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.user.User;
import javassist.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Optional;



public interface AnswerDtoDao {
    Optional<AnswerDto> getById(AnswerDto answerDto);
    Optional<AnswerDto> updateAnswer(AnswerDto answerDto, Long answerId, User user) throws NotFoundException, AccessDeniedException;
}
