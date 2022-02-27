package com.chatappapi.api.util;

import com.chatappapi.api.config.SecurityConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(SecurityConstants.KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
