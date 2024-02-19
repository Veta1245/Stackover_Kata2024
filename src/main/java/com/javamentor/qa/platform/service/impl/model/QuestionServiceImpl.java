package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private final QuestionDao questionDao;

    public QuestionServiceImpl(ReadWriteDao<Question, Long> readWriteDao, QuestionDao questionDao) {
        super(readWriteDao);
        this.questionDao = questionDao;
    }

    @Override
    @Transactional
    public Question addNewQuestion(QuestionCreateDto questionCreateDto, List<Tag> tag, User user) {
        Question question = new Question();
        question.setTitle(questionCreateDto.getTitle());
        question.setDescription(questionCreateDto.getDescription());
        question.setTags(tag);
        question.setUser(user);
        questionDao.saveQuestion(question);
        return question;
    }
}
