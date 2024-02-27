package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {
    private final AnswerDao answerDao;


    public AnswerServiceImpl(ReadWriteDao<Answer, Long> readWriteDao, AnswerDao answerDao) {
        super(readWriteDao);
        this.answerDao = answerDao;
    }

    @Override
    public Answer getByAnswerIdWithoutUser(Long answerId, User user) {
        /**
         * Ответ, с исключением текущего юзера(юзер не может голосовать за себя)
         */
        return answerDao.getByAnswerIdWithoutUser(answerId, user).orElse(null);

    }

    @Override
    public Answer getAnswerById(Long answerId, User user) {

        return answerDao.getAnswerById(answerId, user).orElse(null);
    }

    @Override
    @Transactional
    public Answer getAndMarkForDeleteAnswer(Long Id) {
        Answer answer = answerDao.getById(Id).orElse(null);
        if (answer != null) {
            answer.setIsDeleted(true);
            answer.setIsDeletedByModerator(true);
            answerDao.update(answer);
        }
        return answer;
    }
}
