package com.chatappapi.api.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${api-chatapp.key-secret}")
    private String keySecret;

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(keySecret).parseClaimsJws(token).getBody().getSubject();
    }
}
