package com.shorturl.com.shorturl;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/rest/url")
@RestController
public class ShortUrlResource {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private CustomUrlMappingRepository customUrlMappingRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ShortUrlResource(CustomUrlMappingRepository customUrlMappingRepository, JdbcTemplate jdbcTemplate) {
        this.customUrlMappingRepository = customUrlMappingRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Long> accessCountMap = new HashMap<>();
    @PostMapping
    public String create(@RequestBody String originalUrl) {

        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );

        if (urlValidator.isValid(originalUrl)) {
            String alias = Hashing.murmur3_32().hashString(originalUrl, StandardCharsets.UTF_8).toString();
            System.out.println("URL Id generated: "+ alias);

            redisTemplate.opsForValue().set(alias, originalUrl);

            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setShortUrlAlias(alias);
            urlMapping.setOriginalUrl(originalUrl);
            customUrlMappingRepository.save(urlMapping);

            System.out.println("Entity saved successfully. Alias: " + alias);

            return alias;
        }

        throw new RuntimeException("URL Invalid: " + originalUrl);
    }

    @GetMapping("/{alias}")
    public ResponseEntity<Map<String, Object>> getUrl(@PathVariable String alias) {
        Instant start = Instant.now();

        try {
            UrlMapping urlMapping = customUrlMappingRepository.findByAlias(alias);

            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);

            accessCountMap.put(alias, accessCountMap.getOrDefault(alias, 0L) + 1);

            Map<String, Object> response = new HashMap<>();
            response.put("url", urlMapping.getOriginalUrl());
            response.put("statistics", Map.of("time_taken", timeElapsed.toMillis() + "ms"));

            return ResponseEntity.ok(response);
        } catch (EmptyResultDataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("err_code", "002");
            response.put("description", "SHORTENED URL NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/top10")
    public List<UrlMapping> getTop10() {
        return customUrlMappingRepository.findTop10ByUrlOrderByIdDesc();
    }
}
