package com.vamshi.controller;

import com.vamshi.dto.AnalyticsResponse;
import com.vamshi.dto.CreateShortUrlRequest;
import com.vamshi.dto.ShortUrlResponse;
import com.vamshi.service.UrlService;
import com.vamshi.ratelimit.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final RateLimitService rateLimitService;

    // 🔹 Create Short URL with Rate Limiting
    @PostMapping
    public ResponseEntity<?> createShortUrl(
            @RequestBody CreateShortUrlRequest request,
            HttpServletRequest httpRequest) {

        String clientIp = httpRequest.getRemoteAddr();

        // 🚦 Rate limit check
        if (!rateLimitService.allowRequest(clientIp)) {
            return ResponseEntity
                    .status(429)
                    .body("Rate limit exceeded. Try again after 1 minute.");
        }

        String shortUrl = urlService.createShortUrl(request);

        return ResponseEntity.ok(new ShortUrlResponse(shortUrl));
    }

    // 🔹 Analytics API
    @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @PathVariable String shortCode) {

        AnalyticsResponse response = urlService.getAnalytics(shortCode);

        return ResponseEntity.ok(response);
    }
}
