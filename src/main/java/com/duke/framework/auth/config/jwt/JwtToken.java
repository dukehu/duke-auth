package com.duke.framework.auth.config.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created duke on 2018/8/10
 */
public class JwtToken extends UsernamePasswordAuthenticationToken {

    private String userId;

    private String username;

    private String accessToken;

    private String refreshToken;

    private Map<String, Object> additionalInformation = Collections.emptyMap();

    public JwtToken(Object principal,
                    Object credentials,
                    String userId,
                    String accessToken,
                    String refreshToken) {
        super(principal, credentials);
        this.userId = userId;
        this.username = principal.toString();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtToken(Object principal,
                    Object credentials,
                    Collection<? extends GrantedAuthority> authorities,
                    String userId,
                    String accessToken,
                    String refreshToken) {
        super(principal, credentials, authorities);
        this.userId = userId;
        this.username = principal.toString();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
