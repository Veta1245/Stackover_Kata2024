package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import javassist.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.file.AccessDeniedException;
import java.util.Optional;


@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final AnswerDao answerDao;


    public AnswerDtoDaoImpl(AnswerDao answerDao) {
        this.answerDao = answerDao;

    }

    @Override
    public Optional<AnswerDto> getById(AnswerDto answerDto) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        """
                                SELECT DISTINCT new com.javamentor.qa.platform.models.dto.AnswerDto(
                                    answer.id AS id,
                                    answer.user.id AS userId,
                                    answer.question.id AS questionId,
                                    answer.htmlBody AS body,
                                    answer.persistDateTime AS persistDate,
                                    answer.isHelpful AS isHelpful,
                                    answer.dateAcceptTime AS dateAccept,
                                    COUNT(voteAnswer) AS countValuable,
                                    CAST(SUM(reputation.count) AS long) AS countUserReputation,
                                    user.imageLink AS image,
                                    user.nickname AS nickname,
                                    CAST(
                                        (
                                            (SELECT COUNT(voteAnswer) FROM VoteAnswer voteAnswer WHERE voteAnswer.user = user)
                                        )
                                    AS long) AS countVote,
                                    voteAnswer.voteType AS voteType)
                                FROM Answer answer
                                LEFT JOIN User user ON user.id = answer.user
                                LEFT JOIN Question question ON question.id = answer.question
                                LEFT JOIN Reputation reputation ON reputation.answer = answer.id
                                LEFT JOIN VoteAnswer voteAnswer ON voteAnswer.answer = answer.id
                                WHERE answer.id = :id
                                GROUP BY answer.id, user.imageLink, user.nickname, answer.user.id, user.id, voteAnswer.voteType                   
                                """,
                        AnswerDto.class)
                .setParameter("id", answerDto.getId()));
    }

    @Transactional
    @Override
    public Optional<AnswerDto> updateAnswer(AnswerDto answerDto, Long answerId, User user) throws NotFoundException, AccessDeniedException {

        Optional<Answer> answerOptional = answerDao.getById(answerId);

        if (!answerId.equals(answerDto.getId()) || answerOptional.isEmpty()) {
            throw new NotFoundException("Answer not found");
        }

        if(answerOptional.get().getIsDeleted()) {
            throw new AccessDeniedException("Answer is deleted");
        }

        if(answerDto.getBody().isEmpty()) {
            throw new NullPointerException("Body is empty");
        }

        if (!answerOptional.get().getUser().getEmail().equals(user.getUsername())) {
            throw new AccessDeniedException("Access denied");
        }
        Answer answer = answerOptional.get();
        System.out.println(answer.getId());
        answer.setHtmlBody(answerDto.getBody());
        answer.setUpdateDateTime(answer.getDateAcceptTime());

        answerDao.persist(answer);

        return this.getById(answerDto);
    }
    @Override
    public Optional<AnswerDto> getAllAnswersDtoByQuestionId(Long questionId, Long userId) {
        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("""
    SELECT NEW com.javamentor.qa.platform.models.dto.AnswerDto(
        a.id,
        a.user.id ,
        a.question.id,
        a.htmlBody,
        a.persistDateTime,
        a.isHelpful,
        a.dateAcceptTime,
        COALESCE(SUM(CASE WHEN va.voteType = 'UP' THEN 1 WHEN va.voteType = 'DOWN' THEN -1 ELSE 0 END), 0),
        COALESCE(SUM(r.count), 0),
        a.user.imageLink,
        a.user.nickname,
        COUNT(va.id),
        (SELECT va.voteType FROM VoteAnswer va WHERE va.answer.id = a.id AND va.user.id = :userId)
    )
    FROM Answer a
    LEFT JOIN a.voteAnswers va
    LEFT JOIN Reputation r ON r.author.id = a.user.id
    WHERE a.question.id = :questionId AND a.isDeleted = false
    GROUP BY a.id, a.user.id, a.question.id, a.htmlBody, a.persistDateTime, a.isHelpful,
             a.dateAcceptTime, a.user.imageLink, a.user.nickname
    """, AnswerDto.class)
                .setParameter("questionId", questionId)
                .setParameter("userId", userId));
    }

}
