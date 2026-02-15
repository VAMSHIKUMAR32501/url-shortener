package com.vamshi.repository;

import com.vamshi.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
    List<Url> findByExpirationDateBefore(LocalDateTime time);
    //void deleteAll(List<Url> urls);
}
