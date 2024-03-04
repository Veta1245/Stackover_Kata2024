package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Answer> getAnswerById(Long answerId, User user) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("""
                      SELECT a FROM Answer a WHERE a.id =: answerId AND a.user.id !=: userId""", Answer.class)
                .setParameter("answerId", answerId)
                .setParameter("userId", user.getId()));
    }
    @Override
    public Optional<Answer> getByAnswerIdWithoutUser(Long answerId, User user) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("""
                SELECT an FROM Answer an WHERE an.id = :id AND an.user.id != :userId""",Answer.class)
                .setParameter("id", answerId)
                .setParameter("userId", user.getId()));
    }
}