package com.example.interceptorhandler.configuration;

import com.example.interceptorhandler.configuration.filter.RequestWrapperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private ApiRequestInterceptor apiRequestInterceptor;

    @Autowired
    private CustomInterceptor customInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiRequestInterceptor).addPathPatterns("/**/api/v1/**");
    }

    @Bean
    public FilterRegistrationBean<RequestWrapperFilter> loggingFilter(){
        FilterRegistrationBean<RequestWrapperFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

}