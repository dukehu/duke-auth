package com.duke.framework.auth.config;

import com.duke.framework.auth.AuthProperties;
import com.duke.framework.auth.config.jwt.JwtAuthenticationTokenFilter;
import com.duke.framework.auth.service.AuthUserDetailService;
import com.duke.framework.auth.service.LoginLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created duke on 2018/7/24
 */
@Configuration
@EnableWebSecurity // 启用web安全
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启security注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthUserDetailService authUserDetailService;

    @Autowired
    private LoginLockService loginLockService;

    @Autowired
    private AuthProperties authProperties;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthAuthenticationProvider(authProperties, passwordEncoder(), authUserDetailService, loginLockService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                .antMatchers("/sign_in").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        // @formatter:on
    }
}
