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
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coopera.cooperaApp.security.SecurityUtils.JWT_SECRET;
@AllArgsConstructor
@Getter
public class JwtUtil {

    @Value(JWT_SECRET)
    private String secret;

    public  Map<String, Claim> extractClaimsFromToken(String token) throws CooperaException {
        DecodedJWT decodedJwt = validateToken(token);
        return decodedJwt.getClaims();
    }

    public DecodedJWT validateToken(String token){
        System.out.println(token);
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build().verify(token);
    }

    public DecodedJWT verifyToken(String token) {
        System.out.println("Token 2"+ token);
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String generateAccessToken(String id) {
//        roles.add(newRole);
//        Map<String, String> map = new HashMap<>();
//        int number = 1;
//        for (int i = 0; i < roles.size(); i++) {
//            map.put("role"+number, roles.toArray()[i].toString());
//            number++;
//        }
        return JWT.create().withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(86000L)).
                withClaim("id", id).sign(Algorithm.HMAC512(secret.getBytes()));
    }

}
