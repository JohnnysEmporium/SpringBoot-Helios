package com.bulb.javabulb.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import static java.util.Arrays.stream;

public class JWTHelper {

    private static final Algorithm algorithm = AlgorithmHolder.algorithm;
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

    public static void setSecurityContextAuthorization(String header){
        Map.Entry<String, Collection<SimpleGrantedAuthority>> userData = getUsernameWithAuthorities(header).entrySet().iterator().next();
        String username = userData.getKey();
        Collection<SimpleGrantedAuthority> authorities = userData.getValue();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public static String getUsername(String header){
        return decode(header).getSubject();
    }

    public static Map<String, Collection<SimpleGrantedAuthority>> getUsernameWithAuthorities(String header){
        Map<String, Collection<SimpleGrantedAuthority>> map = new HashMap<>();
        DecodedJWT d = decode(header);
        String username = d.getSubject();
        String[] roles = d.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        map.put(username, authorities);
        return map;
    }

    private static DecodedJWT decode(String header){
        String token = header.substring("Bearer ".length());
        return verifier.verify(token);
    }

/* One body function for reference:
    String token = authorizationHeader.substring("Bearer ".length());
    Algorithm algorithm = AlgorithmHolder.algorithm;
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    String username = decodedJWT.getSubject();
    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    stream(roles).forEach( role -> {
       authorities.add(new SimpleGrantedAuthority(role));
    });
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
    System.out.println("AUTHTOKEN: " + authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
 */
}
