package com.company_management.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppConfig {
    @Value("${app.config.jwt.secret-key}")
    private String JWTSecretKey;

    @Value("${app.config.jwt.expire-time}")
    private int JWTExpireTime;

    @Value("${app.config.max-number-password-wrong}")
    private int maxNumPassWrong;

    @Value("${app.config.password-expired-time}")
    private int passwordExpireTime;

    @Value("${app.config.cache.expire-time}")
    private int cacheExpireTime;

    @Value("${app.config.cache.initial-size}")
    private int initSize;

    @Value("${app.config.cache.maximun-size}")
    private int maxSize;
}
