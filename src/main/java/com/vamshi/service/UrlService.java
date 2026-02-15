package com.vamshi.service;

import com.vamshi.dto.AnalyticsResponse;
import com.vamshi.dto.CreateShortUrlRequest;
import com.vamshi.entity.Url;
import com.vamshi.exception.UrlNotFoundException;
import com.vamshi.repository.UrlRepository;
import com.vamshi.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final Base62Encoder encoder;

    public String createShortUrl(CreateShortUrlRequest request) {

        // 🔹 If custom alias provided
        if (request.getCustomAlias() != null && !request.getCustomAlias().isBlank()) {

            if (urlRepository.existsByShortCode(request.getCustomAlias())) {
                throw new RuntimeException("Custom alias already in use");
            }

            Url url = Url.builder()
                    .originalUrl(request.getOriginalUrl())
                    .shortCode(request.getCustomAlias())
                    .createdAt(LocalDateTime.now())
                    .expirationDate(request.getExpirationDate())
                    .clickCount(0L)
                    .build();

            urlRepository.save(url);

            return "http://localhost:8080/" + request.getCustomAlias();
        }

        // 🔹 Otherwise generate normally
        Url url = Url.builder()
                .originalUrl(request.getOriginalUrl())
                .createdAt(LocalDateTime.now())
                .expirationDate(request.getExpirationDate())
                .clickCount(0L)
                .build();

        Url saved = urlRepository.save(url);

        String shortCode = encoder.encode(saved.getId());
        saved.setShortCode(shortCode);

        urlRepository.save(saved);

        return "http://localhost:8080/" + shortCode;
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public String getOriginalUrl(String shortCode) {
    	  System.out.println("Fetching from DATABASE...");
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        if (url.getExpirationDate() != null &&
                url.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException("URL expired");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getOriginalUrl();
    }
    public AnalyticsResponse getAnalytics(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        return new AnalyticsResponse(
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpirationDate()
        );
    }

}
