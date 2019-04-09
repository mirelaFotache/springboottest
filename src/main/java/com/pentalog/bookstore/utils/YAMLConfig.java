package com.pentalog.bookstore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class YAMLConfig {
    private static Logger logger = LoggerFactory.getLogger(YAMLConfig.class);

    @Value("${booking.max.allowed}")
    private Integer maxBookingsAllowed;

    public Integer getMaxBookingsAllowed() {
        return maxBookingsAllowed;
    }

    @PostConstruct
    public void postConstruct(){
        logger.info("maxBookingsAllowed : "+maxBookingsAllowed);
    }

    //@EnableConfigurationProperties and @ConfigurationProperties are used in test files for example...
}
