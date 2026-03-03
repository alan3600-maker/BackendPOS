package py.com.hidraulica.caacupe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  private final Key key;
  private final long expirationMillis;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-minutes:480}") long expirationMinutes
  ) {
    if (secret == null || secret.length() < 32) {
      throw new IllegalStateException("app.jwt.secret debe tener al menos 32 caracteres.");
    }
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMillis = expirationMinutes * 60_000L;
  }

  public String generateToken(String subject, Map<String, Object> claims) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(subject)
        .claims(claims)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(expirationMillis)))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return parseAllClaims(token).getSubject();
  }

  public boolean isTokenValid(String token, String username) {
    Claims c = parseAllClaims(token);
    return username.equals(c.getSubject()) && c.getExpiration().after(new Date());
  }

  private Claims parseAllClaims(String token) {
    return Jwts.parser()
        .verifyWith((javax.crypto.SecretKey) key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
