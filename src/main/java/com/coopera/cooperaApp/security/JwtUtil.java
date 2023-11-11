package com.coopera.cooperaApp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.coopera.cooperaApp.enums.Role;
import com.coopera.cooperaApp.exceptions.CooperaException;
import com.coopera.cooperaApp.models.Member;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.coopera.cooperaApp.security.SecurityUtils.JWT_SECRET;
@AllArgsConstructor
public class JwtUtil {

    @Value(JWT_SECRET)
    private String secret;

    public  Map<String, Claim> extractClaimsFromToken(String token) throws CooperaException {
        DecodedJWT decodedJwt = validateToken(token);
        return decodedJwt.getClaims();
    }

    private DecodedJWT validateToken(String token){
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build().verify(token);
    }

    public DecodedJWT verifyToken(String token) {
        System.out.println("Hello");
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String generateAccessToken(Member member, Role role){
        var listOfCurrentUserRoles = member.getRoles();
        listOfCurrentUserRoles.add(role);
        Map<String, String> map = new HashMap<>();
        int number = 1;
        for (int i = 0; i < listOfCurrentUserRoles.size(); i++) {
            map.put("role"+number, listOfCurrentUserRoles.toArray()[i].toString());
            number++;
        }
        return JWT.create().withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(86000L)).
                withClaim("Roles", map).withClaim("userId", member.getId()).sign(Algorithm.HMAC512(secret.getBytes()));
    }
}
