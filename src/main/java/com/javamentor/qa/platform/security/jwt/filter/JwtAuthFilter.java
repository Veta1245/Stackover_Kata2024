package com.javamentor.qa.platform.security.jwt.filter;

import com.javamentor.qa.platform.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Установка аутентификации и авторизации пользователя с использованием JWT токена
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt =  getTokenFromRequest(request);
        if (jwt != null) {
            try {
                String username = jwtUtil.getUsernameFromToken(jwt);
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(username));
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     * Извлекаем JWT токен из HTTP запроса
     * @param request
     * @return токен из HTTP запроса
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
            //удаляем "Bearer " из строки
        }
        return null;
    }

    /**
     * Объект аутентификации на основе данных пользователя
     * @param username
     * @return аутентификация пользователя
     */
    private Authentication getAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                null, userDetails.getAuthorities());
    }
}
