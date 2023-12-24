package com.mail.backend.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;

import com.mail.backend.Managers.UserManager;
import com.mail.backend.Models.User.User;

import java.security.Key;
import java.util.Base64;

public class Auth {

    static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());

    public static String getToken(String username) {
        return Jwts.builder()
                .setId(username)
                .setSubject("account")
                .signWith(hmacKey)
                .compact();
    }

    public static String getUserId(String jwtString) {

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt.getBody().getId();
    }

    public static User getUser(String jwtString) {


        String username = getUserId(jwtString);
        return UserManager.getInstance().getUser(username);
    }
}