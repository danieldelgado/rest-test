package com.bci.reactive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends
        ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("api-resource");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
               // .antMatchers("/*").permitAll()
               /* .anyRequest()*/
        .antMatchers("/index.html", "/login","/v2/api-docs").permitAll()
        .and()
                .authorizeRequests().antMatchers("/resources/**").permitAll()
                .antMatchers("/*.js").permitAll().anyRequest().permitAll()
                .antMatchers("/*.css").permitAll().anyRequest().permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v{[0-9]+}/*").hasRole("USER")
                .anyRequest()
                .authenticated()

        ;
    }
}