package twilio_spa_fetch_backend.util;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Encodes Twilio next-page URLs as opaque page tokens.
 *
 * The Twilio SDK paginates with full URLs. Decoding validates the URL points at
 * twilio.com over HTTPS so a forged token cannot make the backend fetch an
 * arbitrary URL with the user's credentials (SSRF).
 */
public final class PageTokenCodec {

    private PageTokenCodec() {
    }

    public static String encode(String pageUrl) {
        if (pageUrl == null) {
            return null;
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(pageUrl.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String pageToken) {
        String url;
        try {
            url = new String(Base64.getUrlDecoder().decode(pageToken), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid page token");
        }
        URI uri = URI.create(url);
        String host = uri.getHost();
        boolean isTwilioHost = host != null && (host.equals("twilio.com") || host.endsWith(".twilio.com"));
        if (!"https".equals(uri.getScheme()) || !isTwilioHost) {
            throw new IllegalArgumentException("Invalid page token");
        }
        return url;
    }
}
