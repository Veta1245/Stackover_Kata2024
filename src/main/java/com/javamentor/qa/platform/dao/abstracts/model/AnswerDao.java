package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface AnswerDao extends ReadWriteDao<Answer, Long> {
    Optional<Answer> getByAnswerIdWithoutUser(Long answerId, User user);
    Optional<Answer> getAnswerById(Long answerId, User user);
}
