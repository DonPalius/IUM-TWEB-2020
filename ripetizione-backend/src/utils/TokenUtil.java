package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

// We need a signing key, so we'll create one just for this example. Usually
// the key would be read from your application configuration instead.


public class TokenUtil {
    public static String generateToken(String id, String name){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        String jws = Jwts.builder()
                .setSubject(name)
                .setId(id)
                .setIssuedAt(now)
                .signWith(key)
                .compact();

        return jws;
    }
}
