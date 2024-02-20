package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
public class AnswerDtoServiceImpl implements AnswerDtoService {
    private final AnswerDtoDao answerDtoDao;

    public AnswerDtoServiceImpl(AnswerDtoDao answerDtoDao) {
        this.answerDtoDao = answerDtoDao;
    }

    @Override
    public Optional<AnswerDto> getById(AnswerDto answerDto) {
        return answerDtoDao.getById(answerDto);
    }

    @Override
    public Optional<AnswerDto> updateAnswer(AnswerDto answerDto, Long answerId, User user) throws AccessDeniedException, NotFoundException {
        return answerDtoDao.updateAnswer(answerDto, answerId, user);
    }

}
