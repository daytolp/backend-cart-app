package com.daytolp.cartapp.backend_cart_app.auth.providers;

import javax.crypto.SecretKey;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;  
import io.jsonwebtoken.security.Keys; 


@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public SecretKey getKey() {  
        byte[] secretBytes = Decoders.BASE64URL.decode(secretKey);  
        return Keys.hmacShaKeyFor(secretBytes);  
    }
}
