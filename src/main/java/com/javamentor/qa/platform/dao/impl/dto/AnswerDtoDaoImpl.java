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

}