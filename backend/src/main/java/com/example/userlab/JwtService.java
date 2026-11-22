package com.example.userlab;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class JwtService {
private final SecretKey key;
public JwtService(@Value("${JWT_SECRET_B64}") String secret) {
this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
}
public String issue(String username) {
long now=System.currentTimeMillis();
return Jwts.builder().subject(username).issuedAt(new Date(now))
.expiration(new Date(now+15*60*1000L)).signWith(key).compact();
}
public String subject(String token) {
return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
.getPayload().getSubject();
}
}
