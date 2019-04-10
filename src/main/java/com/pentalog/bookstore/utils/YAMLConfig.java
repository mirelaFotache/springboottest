package com.pentalog.bookstore.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="bookings")
public class YAMLConfig {

    private Integer maxAllowed;

    public YAMLConfig() {
    }

    public Integer getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(Integer maxAllowed) {
        this.maxAllowed = maxAllowed;
    }
}
