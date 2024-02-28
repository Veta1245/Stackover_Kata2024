package com.javamentor.qa.platform.webapp.controllers.rest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.JmApplicationTests;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ResourceAnswerControllerTest extends JmApplicationTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    @DataSet(value = "dataset/ResourceAnswerControllerTest/downVoteAnswer.yml", cleanBefore = true, cleanAfter = true)
    void successfulDownVoteForAnswer() throws Exception {
        VoteAnswer voteAnswerBefore = entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.id = 110", VoteAnswer.class)
                .getResultList().stream().findFirst().orElse(null);
        Reputation reputationBefore = entityManager.createQuery("SELECT r FROM Reputation r WHERE r.answer.id = 101 AND r.sender.id = 100", Reputation.class).getSingleResult();
        Assertions.assertNull(voteAnswerBefore);
        mockMvc.perform(post("/api/user/question/{questionId}/answer/{answerId}/downVote",101L, 101L)
                .header("Authorization", "Bearer " + obtainAccessToken("user1@gmail.com", "123456")))
                .andExpect(status().isOk());

        VoteAnswer voteAnswerAfter = entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.user.id = 100 AND va.answer.id = 101", VoteAnswer.class).getSingleResult();
        Reputation reputationAfter = entityManager.createQuery("SELECT r FROM Reputation r WHERE r.answer.id = 101 AND r.sender.id = 100", Reputation.class).getSingleResult();

        Assertions.assertNotNull(voteAnswerAfter);
        Assertions.assertNotNull(reputationAfter);

        Assertions.assertEquals(VoteType.DOWN, voteAnswerAfter.getVoteType());

        Assertions.assertNotEquals(-5, reputationBefore.getCount() - reputationAfter.getCount());
    }

    @Test
    @DataSet(value = "dataset/ResourceAnswerControllerTest/downVoteAnswer.yml", cleanBefore = true, cleanAfter = true)
    void successfulDownVoteOnAnAnswerWithAlreadyAnExistingUpvoteType() throws Exception {
        VoteAnswer voteAnswerBefore = entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.id = 100", VoteAnswer.class).getSingleResult();
        Reputation reputationBefore = entityManager.createQuery("SELECT r FROM Reputation r WHERE r.answer.id = 100 AND r.sender.id = 101", Reputation.class).getSingleResult();
        Assertions.assertNotNull(voteAnswerBefore);
        mockMvc.perform(post("/api/user/question/{questionId}/answer/{answerId}/downVote",100L, 100L)
                        .header("Authorization", "Bearer " + obtainAccessToken("user2@gmail.com", "123456")))
                .andExpect(status().isOk());

        VoteAnswer voteAnswerAfter = entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.user.id = 100 AND va.answer.id = 101", VoteAnswer.class).getSingleResult();
        Reputation reputationAfter = entityManager.createQuery("SELECT r FROM Reputation r WHERE r.answer.id = 100 AND r.sender.id = 101", Reputation.class).getSingleResult();

        Assertions.assertNotNull(voteAnswerAfter);
        Assertions.assertNotNull(reputationAfter);

        Assertions.assertEquals(VoteType.UP, voteAnswerBefore.getVoteType());
        Assertions.assertEquals(VoteType.DOWN, voteAnswerAfter.getVoteType());

        Assertions.assertNotEquals(-5, reputationBefore.getCount() - reputationAfter.getCount());
    }

    @Test
    @DataSet(value = "dataset/ResourceAnswerControllerTest/downVoteAnswer.yml", cleanBefore = true, cleanAfter = true)
    void downVoteForNonExistenAnswer() throws Exception {
        mockMvc.perform(post("/api/user/question/{questionId}/answer/{answerId}/downVote",100L, 110L)
                .header("Authorization", "Bearer " + obtainAccessToken("user1@gmail.com", "123456")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = "dataset/ResourceAnswerControllerTest/downVoteAnswer.yml", cleanBefore = true, cleanAfter = true)
    void tryingToDownVoteYourOwnAnswer() throws Exception {
        VoteAnswer voteAnswerBefore = entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.id = 110", VoteAnswer.class)
                .getResultList().stream().findFirst().orElse(null);
        Reputation reputationBefore = entityManager.createQuery("SELECT r FROM Reputation r WHERE r.answer.id = 100 AND r.sender.id = 100", Reputation.class).getSingleResult();
        Assertions.assertNull(voteAnswerBefore);
        mockMvc.perform(post("/api/user/question/{questionId}/answer/{answerId}/downVote",101L, 100L)
                .header("Authorization", "Bearer " + obtainAccessToken("user1@gmail.com", "123456")))
                .andExpect(status().isNotFound());

        VoteAnswer voteAnswerAfter = entityManager.createQuery("SELECT va FROM VoteAnswer va WHERE va.user.id = 100 AND va.answer.id = 100", VoteAnswer.class)
                .getResultList().stream().findFirst().orElse(null);
        Reputation reputationAfter = entityManager.createQuery("SELECT r FROM Reputation r WHERE r.answer.id = 100 AND r.sender.id = 100", Reputation.class).getSingleResult();

        Assertions.assertNull(voteAnswerAfter);

        Assertions.assertEquals(reputationBefore.getCount(), reputationAfter.getCount());
    }
}
