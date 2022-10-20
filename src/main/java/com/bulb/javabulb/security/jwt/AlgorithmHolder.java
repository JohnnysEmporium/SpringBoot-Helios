package com.bulb.javabulb.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;

public class AlgorithmHolder {

    //Change "secret" for something really secret when in prod
    public static Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
}
