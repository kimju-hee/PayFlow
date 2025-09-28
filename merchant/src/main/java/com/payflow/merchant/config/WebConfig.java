package com.payflow.merchant.config;

import com.payflow.merchant.infra.IdempotencyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestClient;

@Configuration
public class WebConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

    @Bean
    public FilterRegistrationBean<IdempotencyFilter> idem(StringRedisTemplate redis) {
        FilterRegistrationBean<IdempotencyFilter> r = new FilterRegistrationBean<>();
        r.setFilter(new IdempotencyFilter(redis));
        r.addUrlPatterns("/merchant/api/orders", "/merchant/api/payments/ready");
        r.setOrder(1);
        return r;
    }
}
