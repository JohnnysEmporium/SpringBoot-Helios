package com.bulb.javabulb.security.filter;

import com.bulb.javabulb.security.jwt.JWTHelper;
import com.bulb.javabulb.user.service.UserService;
import com.bulb.javabulb.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

//Every request goes through this, then the API is being invoked

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(
            request.getServletPath().equals("/login") ||
            request.getServletPath().equals("/api/token/refresh")
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

                    e.printStackTrace();
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
