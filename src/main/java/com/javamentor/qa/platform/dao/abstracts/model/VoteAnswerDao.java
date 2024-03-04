package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer, Long> {
    Optional<VoteAnswer> getVoteAnswerByAnswerIdAndUser(Long answerId, Long userId);

    Long getAllTheVotesForThisAnswerUp(Answer answerUp);
    Optional<VoteAnswer> getVoteAnswerByUserAndAnswer(Long answerId, Long userId);

    Long downVoteCount(Answer answer);
}
