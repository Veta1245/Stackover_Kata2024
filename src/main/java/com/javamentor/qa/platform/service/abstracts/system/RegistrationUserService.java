package com.javamentor.qa.platform.service.abstracts.system;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;

public interface RegistrationUserService {
    boolean verifyEmail(String email, String code);

    boolean registerUser(UserRegistrationDto userRegistrationDto);
}
