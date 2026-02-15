package com.vamshi.scheduler;

import com.vamshi.entity.Url;
import com.vamshi.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpirationScheduler {

    private final UrlRepository urlRepository;

    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void deleteExpiredUrls() {

        List<Url> expired =
                urlRepository.findByExpirationDateBefore(LocalDateTime.now());

        if (!expired.isEmpty()) {
            urlRepository.deleteAll(expired);
            System.out.println("Deleted expired URLs: " + expired.size());
        }
    }
}
