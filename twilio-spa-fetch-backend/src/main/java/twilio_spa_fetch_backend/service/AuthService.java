package twilio_spa_fetch_backend.service;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.LoginRequestDTO;
import twilio_spa_fetch_backend.dto.LoginResponseDTO;
import twilio_spa_fetch_backend.exception.InvalidCredentialsException;
import twilio_spa_fetch_backend.security.TokenCipher;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final TokenCipher tokenCipher;

    public AuthService(TokenCipher tokenCipher) {
        this.tokenCipher = tokenCipher;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String accountSid = request.accountSid();
        String authToken = request.authToken();

        // A dedicated client validates the credentials without touching any
        // global SDK state, so concurrent logins never interfere.
        Account account = fetchAccount(accountSid, authToken);
        String token = generateJwtToken(accountSid, authToken);

        return new LoginResponseDTO(
                token,
                accountSid,
                account.getFriendlyName(),
                jwtExpiration / 1000
        );
    }

    private Account fetchAccount(String accountSid, String authToken) {
        try {
            TwilioRestClient client = new TwilioRestClient.Builder(accountSid, authToken).build();
            return Account.fetcher(accountSid).fetch(client);
        } catch (Exception e) {
            log.warn("Twilio credential validation failed for {}: {}", accountSid, e.getMessage());
            throw new InvalidCredentialsException("Invalid Twilio credentials", e);
        }
    }

    private String generateJwtToken(String accountSid, String authToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(accountSid)
                // The auth token is encrypted: JWT claims are readable by anyone
                // holding the token, and this one is a Twilio master credential.
                .claim("authToken", tokenCipher.encrypt(authToken))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getAccountSidFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getAuthTokenFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        String encrypted = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("authToken", String.class);
        return tokenCipher.decrypt(encrypted);
    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
