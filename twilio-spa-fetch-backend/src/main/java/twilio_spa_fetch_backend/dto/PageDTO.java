package twilio_spa_fetch_backend.dto;

import java.util.List;

/**
 * Cursor-based page of results. {@code nextPageToken} is an opaque token to be
 * sent back as the {@code pageToken} query parameter; {@code null} on the last page.
 */
public record PageDTO<T>(
        List<T> items,
        String nextPageToken
) {}
