package twilio_spa_fetch_backend.security;

import com.twilio.http.TwilioRestClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds a {@link TwilioRestClient} scoped to the authenticated user's credentials.
 *
 * The Twilio SDK's static {@code Twilio.init(...)} configures a single global client,
 * which is unsafe when concurrent requests belong to different Twilio accounts.
 * Every service must obtain its client from this provider and pass it explicitly
 * to the SDK's fetcher/reader/creator calls.
 */
@Component
public class TwilioClientProvider {

    private final Map<String, TwilioRestClient> clientCache = new ConcurrentHashMap<>();

    public TwilioRestClient getClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getCredentials() instanceof String authToken)) {
            throw new IllegalStateException("No authenticated Twilio credentials in the request context");
        }
        String accountSid = authentication.getPrincipal().toString();
        return getClient(accountSid, authToken);
    }

    public TwilioRestClient getClient(String accountSid, String authToken) {
        // Keyed by SID + token hash so a rotated auth token yields a fresh client.
        String cacheKey = accountSid + ':' + Integer.toHexString(authToken.hashCode());
        return clientCache.computeIfAbsent(cacheKey,
                key -> new TwilioRestClient.Builder(accountSid, authToken).build());
    }
}
