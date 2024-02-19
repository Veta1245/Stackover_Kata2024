package com.javamentor.qa.platform.security.jwt.service;

import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.javamentor.qa.platform.models.dto.TokenResponseDTO;
import com.javamentor.qa.platform.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService{
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public Optional<TokenResponseDTO> authenticateUser(AuthenticationRequestDTO authenticationRequestDTO) {

        /**
         * Аутентификация пользователя с использованием предоставленного логина и пароля
         */
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getLogin(), authenticationRequestDTO.getPass()));
        } catch (AuthenticationException e) {
            return Optional.empty();
        }

        /**
         * Загрузка данных пользователя из сервиса пользователей,
         * создание объекта TokenResponseDTO с информацией о пользователе и токеном
         * возвращает объект в виде Optional.
         */
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDTO.getLogin());
        return Optional.of(new TokenResponseDTO(
                userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null),
                jwtUtil.generateToken(userDetails)));
    }
}