package com.payflow.pg.config;

import com.payflow.pg.infra.IdempotencyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
    private final StringRedisTemplate redis;
    @Bean public RestClient restClient(){ return RestClient.builder().build(); }
    @Bean
    public FilterRegistrationBean<IdempotencyFilter> idem(){
        FilterRegistrationBean<IdempotencyFilter> r = new FilterRegistrationBean<>();
        r.setFilter(new IdempotencyFilter(redis));
        r.addUrlPatterns("/payments/ready", "/payments/approve");
        r.setOrder(1);
        return r;
    }
}
