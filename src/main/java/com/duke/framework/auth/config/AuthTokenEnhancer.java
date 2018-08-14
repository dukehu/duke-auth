package com.duke.framework.auth.config;

import com.duke.framework.auth.config.jwt.JwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created duke on 2018/8/9
 *
 * token 增强器
 */
public class AuthTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
            Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
            additionalInformation.put("loginName", oAuth2Authentication.getName());
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            if (authentication instanceof JwtToken) {
                JwtToken jwtToken = (JwtToken) authentication;
                additionalInformation.put("userId", jwtToken.getUserId());
                additionalInformation.putAll(jwtToken.getAdditionalInformation());
            }
            token.setAdditionalInformation(additionalInformation);
        }
        return accessToken;
    }
}
