package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.service.abstracts.repository.ReadWriteService;

public interface ReputationService extends ReadWriteService<Reputation, Long> {
    Reputation getDownReputationByAnswerAndUser(Long answerId, User user);
}
