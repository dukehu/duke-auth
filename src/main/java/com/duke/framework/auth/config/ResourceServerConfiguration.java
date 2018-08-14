package com.duke.framework.auth.config;

import com.duke.framework.config.security.AuthAccessDeniedHandler;
import com.duke.framework.config.security.AuthUnauthorizedEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器相关配置
 * <p>
 * Created duke on 2018/7/24
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "duke-auth";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(DEMO_RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                // .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .authenticationEntryPoint(new AuthUnauthorizedEntryPoint(objectMapper))
                .accessDeniedHandler(new AuthAccessDeniedHandler(objectMapper))
                .and()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/sign_in").permitAll()
                .antMatchers("/sign_out").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
