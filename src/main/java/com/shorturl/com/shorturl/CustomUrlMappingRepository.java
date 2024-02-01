package com.shorturl.com.shorturl;

import com.shorturl.com.shorturl.UrlMapping;

import java.util.List;

public interface CustomUrlMappingRepository {
    void save(UrlMapping urlMapping);
    UrlMapping findByAlias(String alias);
    List<UrlMapping> findTop10ByUrlOrderByIdDesc();
}

