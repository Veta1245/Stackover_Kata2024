package com.javamentor.qa.platform.security.jwt.service;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;

import java.util.Optional;

public interface JwtAuthService {
    Optional<TokenResponseDTO> authenticateUser(AuthenticationRequestDTO authenticationRequestDTO);
}