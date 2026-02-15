package com.vamshi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateShortUrlRequest {

    private String originalUrl;
    private String customAlias;   // NEW
    private LocalDateTime expirationDate;
}
