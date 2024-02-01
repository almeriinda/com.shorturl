package com.shorturl.com.shorturl;

import javax.persistence.*;

@Entity
@Table(name = "urls")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalUrl;
    private String alias;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOriginalUrl() {
        return originalUrl;
    }
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    public String getShortUrlAlias() {
        return alias;
    }
    public void setShortUrlAlias(String shortUrlAlias) {
        this.alias = shortUrlAlias;
    }
}
