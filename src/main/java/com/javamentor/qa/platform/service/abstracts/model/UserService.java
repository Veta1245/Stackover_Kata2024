package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.repository.ReadWriteService;

import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {
    Optional<User> getByEmail(String email);

}
