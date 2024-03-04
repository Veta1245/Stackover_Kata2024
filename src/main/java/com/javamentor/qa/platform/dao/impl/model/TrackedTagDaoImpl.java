package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.dao.impl.repository.ReadWriteDaoImpl;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getTheTagCountInATagTracked(Long userId, Long tagId) {
        return entityManager.createQuery("SELECT COUNT(t) FROM TrackedTag t WHERE t.user.id = :userId AND t.trackedTag.id = :tagId", Long.class)
                .setParameter("userId", userId)
                .setParameter("tagId", tagId)
                .getSingleResult();
    }
}
