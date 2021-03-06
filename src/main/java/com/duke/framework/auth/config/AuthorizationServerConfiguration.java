package com.duke.framework.auth.config;

import com.duke.framework.auth.service.AuthUserDetailService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务相关配置
 * <p>
 * Created duke on 2018/7/24
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "duke-auth";

    @Autowired
    private AuthUserDetailService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HikariDataSource hikariDataSource;


    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new AuthTokenEnhancer();
    }

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(hikariDataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(hikariDataSource);
//        clients.inMemory()
//                .withClient("client_2")
//                .resourceIds(DEMO_RESOURCE_ID)
//                .authorizedGrantTypes("password", "refresh_token")
//                .scopes("select")
//                .authorities("client")
//                .secret("123456");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // todo 后期将这块改成,，下面的
        // endpoints.tokenServices(authTokenService).tokenEnhancer(authTokenEnhancer);

        // 拿到增强器链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();

        // 往增强器链中添加一个token增强器
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(tokenEnhancer());
        enhancerChain.setTokenEnhancers(enhancers);
        endpoints.tokenEnhancer(enhancerChain);

        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //允许表单认证
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }
}
