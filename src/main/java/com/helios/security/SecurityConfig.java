package com.helios.security;
import com.helios.security.filter.CustomAuthenticationFilter;
import com.helios.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final int bcryptStrength = 10;

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//        return super.authenticationManagerBean();
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(bcryptStrength, new SecureRandom());
    }

    @Bean
    /*
    -----------------------------------------
    |          KINDA IMPORTANT NOTE         |
    -----------------------------------------
    Generally it's better to use method security (f.e. @preAuthorize) at a service level.
    This simplifies stuff when a url an API changes.
    -----------------------------------------
     */
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /* Use in case you want to change the base login path.
            /login comes from Spring, it's the default one.
            If you want to change it, create a new CustomAuthentcationFilter and use .setFilterProcessURL method.
            Replace with new in http.addFilter
         */
        http.authorizeRequests().antMatchers("/login", "api/token/refresh").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/dupa").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/save").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();
        http.apply(AuthenticationDSL.customDSL());
        http.apply(AuthorizationDSL.customDSL());


        return http.build();
    }

    public static class AuthenticationDSL extends AbstractHttpConfigurer<AuthenticationDSL, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new CustomAuthenticationFilter(authenticationManager));
        }

        public static AuthenticationDSL customDSL() {
            return new AuthenticationDSL();
        }
    }

    public static class AuthorizationDSL extends AbstractHttpConfigurer<AuthorizationDSL, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        public static AuthorizationDSL customDSL() {
            return new AuthorizationDSL();
        }
    }

}