package com.najie.exam.zuul.config;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.najie.exam.zuul.filter.AccessTokenFilter;

@Configuration
@EnableZuulProxy
public class ZuulConfig {

    @Bean
    public LocationRewriteFilter locationRewriteFilter() {
        return new LocationRewriteFilter();
    }

    @Bean
    public AccessTokenFilter accessTokenFilter(){
        return new AccessTokenFilter();
    }
}
