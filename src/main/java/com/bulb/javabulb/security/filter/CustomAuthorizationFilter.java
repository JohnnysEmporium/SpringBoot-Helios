package com.bulb.javabulb.security.filter;

import com.bulb.javabulb.security.jwt.JWTHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(
            request.getServletPath().equals("/login") ||
            request.getServletPath().equals("api/token/refresh")
        ){
            //Do nothing
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    JWTHelper.setSecurityContextAuthorization(authorizationHeader);
                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    log.error("Error logging in: {}", e.getMessage());
                    Map<String, String> error = new HashMap<>();

                    response.setHeader("error", e.getMessage());
                    error.put("error", e.getMessage());

                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                    filterChain.doFilter(request, response);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
