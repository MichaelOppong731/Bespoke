package com.bespoke.Bespoke.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.TimeoutDeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        WebMvcConfigurer.super.configureAsyncSupport(configurer);
        configurer.setDefaultTimeout(600000);  // Set the default timeout to 10 minutes
        configurer.registerDeferredResultInterceptors(new TimeoutDeferredResultProcessingInterceptor());
    }



}



