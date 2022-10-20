package com.bulb.javabulb.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.Map.Entry;
import static java.util.Arrays.stream;

public class JWTHelper {

    private static final Algorithm algorithm = AlgorithmHolder.algorithm;
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

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

}
