package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReputationServiceImpl extends ReadWriteServiceImpl<Reputation, Long> implements ReputationService {
    private final ReputationDao reputationDao;
    private final AnswerService answerService;

    public ReputationServiceImpl(ReadWriteDao<Reputation, Long> readWriteDao, ReputationDao reputationDao, AnswerService answerService) {
        super(readWriteDao);
        this.reputationDao = reputationDao;
        this.answerService = answerService;
    }

    @Override
    public Reputation getDownReputationByAnswerAndUser(Long answerId, User user) {
        Answer answer = answerService.getByAnswerIdWithoutUser(answerId,user);
        /**
         * Репутация(ответ и отправитель), если null-добавить, не null-обновить
         */
        Reputation reputation = reputationDao.getReputationByAnswerAndUser(answerId,user).orElse(null);

        if (reputation == null) {
            reputation = new Reputation();
            reputation.setPersistDate(LocalDateTime.now());
            reputation.setAuthor(answer.getUser());
            reputation.setSender(user);
            reputation.setCount(-5);
            reputation.setType(ReputationType.VoteAnswer);
            reputation.setQuestion(null);
            reputation.setAnswer(answer);

            reputationDao.persist(reputation);
        } else {
            reputation.setCount(-5);
            reputationDao.update(reputation);
        }
        return reputation;
    }
}
