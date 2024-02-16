package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reputation> getReputationByAnswerAndUser(Long answerId, User user) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("""
        SELECT rp FROM Reputation rp WHERE rp.answer.id = :answerId AND rp.sender.id = :userId 
        """, Reputation.class)
                .setParameter("answerId", answerId)
                .setParameter("userId", user.getId()));
    }
}
