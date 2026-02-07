package twilio_spa_fetch_backend.service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twilio_spa_fetch_backend.dto.LoginRequestDTO;
import twilio_spa_fetch_backend.dto.LoginResponseDTO;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthService {

    @Value("${jwt.secret:secret-key")
    private String jwtSecret;

    @Value("jwt.expiration:86400000")
    private long jwtExpiration;

    public LoginResponseDTO login(LoginRequestDTO request) {
        String accountSid = request.accountSid();
        String authToken = request.authToken();

        if (!validateTwilioCredentials(accountSid, authToken)) {
            throw new RuntimeException("Invalid credentials");
        }

        String accountName = getAccountName(accountSid, authToken);
        String token = generateJwtToken(accountSid, authToken);

        return new LoginResponseDTO (
                token,
                accountSid,
                accountName,
                jwtExpiration / 1000
        );
    }

    private boolean validateTwilioCredentials(String accountSid, String authToken) {
        try {
            Twilio.init(accountSid, authToken);
            Account account = Account.fetcher(accountSid).fetch();
            return account != null;
        } catch (Exception e) {
            System.out.println("Invalid credential: " + e.getMessage());
            return false;
        } finally {
            Twilio.destroy();
        }
    }

    private String getAccountName(String accountSid, String authToken) {
        try {
            Twilio.init(accountSid, authToken);
            Account account = Account.fetcher(accountSid).fetch();
            return account.getFriendlyName();
        } catch (Exception e) {
            return "Twilio Account";
        } finally {
            Twilio.destroy();
        }
    }

    private String generateJwtToken(String accountSid, String authToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(accountSid)
                .claim("authToken", authToken)
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

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("authToken", String.class);
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
