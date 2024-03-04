package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getTop3TagsByUserId(Long userId) {
        return entityManager.createQuery("""
                        select new com.javamentor.qa.platform.models.dto.TagDto
                        (tag.id, tag.name, tag.description)
                        from Tag tag
                        join tag.questions as question
                        join question.answers as answers
                        join answers.user as user
                        join answers.voteAnswers as voteAnswers
                        where user.id = :userId
                        group by tag.id, tag.name, tag.description
                        order by sum(case when voteAnswers.voteType = 'UP' then 1 when voteAnswers.voteType = 'DOWN' then -1 else 0 end)desc""", TagDto.class)
                .setMaxResults(3)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<RelatedTagDto> getTopTags(){
        return entityManager.createQuery("""
                        select new com.javamentor.qa.platform.models.dto.RelatedTagDto
                                 (tag.id, tag.name , count(tag))
                                 from Tag tag
                                 join tag.questions questions
                                 group by tag.id , tag.name
                                 order by count (questions) desc""", RelatedTagDto.class)
                .setMaxResults(10)
                .getResultList();

    }
}
