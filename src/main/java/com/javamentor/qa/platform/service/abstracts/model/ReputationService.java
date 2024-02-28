package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.repository.ReadWriteService;

import java.util.Optional;

public interface ReputationService extends ReadWriteService<Reputation, Long> {
    Reputation getDownReputationByAnswerAndUser(Long answerId, User user);

    Reputation addReputation(Long answerId, User user);

    Optional<Reputation> getReputationByAnswerAndUser(Long answerId, Long userId);
}
