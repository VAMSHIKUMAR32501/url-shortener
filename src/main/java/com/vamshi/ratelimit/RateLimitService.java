package com.vamshi.ratelimit;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW = 60; // seconds

    private final Map<String, RequestData> requestCounts = new ConcurrentHashMap<>();

    public boolean allowRequest(String clientIp) {

        long currentTime = Instant.now().getEpochSecond();

        requestCounts.putIfAbsent(clientIp, new RequestData(0, currentTime));

        RequestData data = requestCounts.get(clientIp);

        if (currentTime - data.startTime > TIME_WINDOW) {
            data.count = 0;
            data.startTime = currentTime;
        }

        if (data.count >= MAX_REQUESTS) {
            return false;
        }

        data.count++;
        return true;
    }

    private static class RequestData {
        int count;
        long startTime;

        RequestData(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
