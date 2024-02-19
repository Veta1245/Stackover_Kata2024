package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {
    private final VoteAnswerDao voteAnswerDao;
    private final AnswerService answerService;
    private final ReputationService reputationService;


    public VoteAnswerServiceImpl(ReadWriteDao<VoteAnswer, Long> readWriteDao, VoteAnswerDao voteAnswerDao, AnswerService answerService, ReputationService reputationService) {
        super(readWriteDao);
        this.voteAnswerDao = voteAnswerDao;
        this.answerService = answerService;
        this.reputationService = reputationService;
    }

    @Override
    @Transactional
    public Long voteUpToAnswer(Long answerId, User user) {
        Answer answer = answerService.getAnswerById(answerId, user);
        reputationService.addReputation(answerId, user);

        VoteAnswer voteAnswer = voteAnswerDao.getVoteAnswerByAnswerIdAndUser(answerId, user).orElse(null);

        if (voteAnswer == null) {
            voteAnswer = new VoteAnswer();
            voteAnswer.setUser(user);
            voteAnswer.setAnswer(answer);
            voteAnswer.setPersistDateTime(LocalDateTime.now());
            voteAnswer.setVoteType(VoteType.UP);
        } else if (voteAnswer.getVoteType() == VoteType.DOWN) {
            voteAnswer.setVoteType(VoteType.UP);
            voteAnswerDao.update(voteAnswer);

        }
        return voteAnswerDao.getAllTheVotesForThisAnswerUp(answer);

    }

    @Transactional
    @Override
    public Long downVoteAnswer(Long answerId, User user) {
        Answer answer = answerService.getByAnswerIdWithoutUser(answerId, user);
        reputationService.getDownReputationByAnswerAndUser(answerId, user);
        /**
         * Голос на ответе
         * голосовал ли юзер за ответ(кто, за какой ответ, лайк(UP) или дизлайк(DOWN)),
         * если голосовал-обновить оценку, не голосовал-сохранить оценку
         */
        VoteAnswer voteAnswer = voteAnswerDao.getVoteAnswerByUserAndAnswer(answerId, user).orElse(null);

        if (voteAnswer == null) {
            voteAnswer = new VoteAnswer();
            voteAnswer.setUser(user);
            voteAnswer.setAnswer(answer);
            voteAnswer.setPersistDateTime(LocalDateTime.now());
            voteAnswer.setVoteType(VoteType.DOWN);

            voteAnswerDao.persist(voteAnswer);
        } else if (voteAnswer.getVoteType() == VoteType.UP) {
            voteAnswer.setVoteType(VoteType.DOWN);

            voteAnswerDao.update(voteAnswer);
        }

        return voteAnswerDao.downVoteCount(answer);
    }
}
