package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Question;

public interface QuestionDao extends ReadWriteDao<Question, Long> {
    void saveQuestion(Question question);
}
