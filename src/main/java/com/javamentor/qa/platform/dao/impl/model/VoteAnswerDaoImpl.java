package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer, Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<VoteAnswer> getVoteAnswerByAnswerIdAndUser(Long answerId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("""
                       SELECT v FROM VoteAnswer v WHERE v.answer.id =: answerId
                        AND v.user.id =:userId""", VoteAnswer.class)
                .setParameter("answerId", answerId)
                .setParameter("userId", userId));
    }

    @Override
    public Long getAllTheVotesForThisAnswerUp(Answer answerUp) {
        return entityManager.createQuery("""
                        select count(*) from VoteAnswer v where v.answer.id = : answerId
                        and v.voteType =: voteType""", Long.class)
                .setParameter("voteType", VoteType.UP)
                .setParameter("answerId", answerUp.getId())
                .getSingleResult();
    }

    @Override
    public Optional<VoteAnswer> getVoteAnswerByUserAndAnswer(Long answerId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "SELECT voan FROM VoteAnswer voan WHERE user.id = :userId AND answer.id =:answerId",
                        VoteAnswer.class)
                .setParameter("userId", userId)
                .setParameter("answerId", answerId));
    }

    @Override
    public Long downVoteCount(Answer answer) {
        return (Long) entityManager.createQuery("""
                        SELECT COUNT(*) FROM VoteAnswer WHERE voteType = :voteType AND answer.id = :answer
                        """)
                .setParameter("voteType", VoteType.DOWN)
                .setParameter("answer", answer.getId()).getSingleResult();
    }
}
