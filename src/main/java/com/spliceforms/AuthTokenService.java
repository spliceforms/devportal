package com.spliceforms;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

@ApplicationScoped
public class AuthTokenService {
    public String createJwt(String jwtSecret, String name, String email, List<String> projects) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setClaim("name", name);
        claims.setClaim("email", email);
        claims.setClaim("allowedProjects", projects);

        claims.setClaim("version", 1);
        claims.setIssuedAtToNow();

        NumericDate expirationTime = NumericDate.now();
        expirationTime.addSeconds(60);

        claims.setExpirationTime(expirationTime);

        HmacKey hmacKey = new HmacKey(jwtSecret.getBytes());
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(hmacKey);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setHeader("typ", "JWT");
        String jwt = jws.getCompactSerialization();

        return jwt;
    }
}
