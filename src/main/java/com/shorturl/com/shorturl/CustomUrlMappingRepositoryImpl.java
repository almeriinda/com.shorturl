package com.shorturl.com.shorturl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomUrlMappingRepositoryImpl implements CustomUrlMappingRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomUrlMappingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(UrlMapping urlMapping) {
        String sql = "INSERT INTO urls (alias, original_url) VALUES (?, ?)";
        jdbcTemplate.update(sql, urlMapping.getShortUrlAlias(), urlMapping.getOriginalUrl());
    }
    @Override
    public UrlMapping findByAlias(String alias) {
        String sql = "SELECT * FROM urls WHERE alias = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new Object[]{alias}, (resultSet, rowNum) -> {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setId(resultSet.getLong("id"));
            urlMapping.setOriginalUrl(resultSet.getString("original_url"));
            urlMapping.setShortUrlAlias(resultSet.getString("alias"));
            return urlMapping;
        });
    }

    @Override
    public List<UrlMapping> findTop10ByUrlOrderByIdDesc() {
        String sql = "SELECT alias, original_url, COUNT(*) as count " +
                "FROM urls " +
                "GROUP BY alias, original_url " +
                "ORDER BY count DESC " +
                "LIMIT 10";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(resultSet.getString("original_url"));
            urlMapping.setShortUrlAlias(resultSet.getString("alias"));
            return urlMapping;
        });
    }

}

