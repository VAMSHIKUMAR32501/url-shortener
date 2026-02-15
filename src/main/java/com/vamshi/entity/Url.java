package com.vamshi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "urls",
       indexes = @Index(name = "idx_short_code", columnList = "shortCode"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(unique = true, length = 50)
    private String shortCode;

    private LocalDateTime createdAt;

    private LocalDateTime expirationDate;

    private Long clickCount;
}
