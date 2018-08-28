package com.duke.framework.auth.config.jwt;

import com.duke.framework.CoreConstants;
import com.duke.framework.auth.AuthProperties;
import com.duke.framework.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created duke on 2018/8/10
 */
@Component
public class JwtTokenProvider {

    @Autowired
    private AuthProperties authProperties;

    public JwtToken createJwtToken(HttpServletRequest request,
                                   HttpServletResponse response,
                                   OAuth2AccessToken oAuth2AccessToken) {
        AuthProperties.Cookie cookie = authProperties.getCookie();
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();

        String accessToken = oAuth2AccessToken.getValue();
        String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        String loginName = additionalInformation.get("loginName").toString();
        String userId = additionalInformation.get("userId").toString();
        String avatar = additionalInformation.get("avatar").toString();

        WebUtils.addCookie(response, CoreConstants.ACCESS_TOKEN, accessToken, cookie.getMaxAge(), cookie.getPath(), cookie.getDomain());
        WebUtils.addCookie(response, CoreConstants.REFRESH_TOKEN, refreshToken, cookie.getMaxAge(), cookie.getPath(), cookie.getDomain());
        WebUtils.addCookie(response, CoreConstants.LOGIN_NAME, loginName, cookie.getMaxAge(), cookie.getPath(), cookie.getDomain());
        WebUtils.addCookie(response, CoreConstants.USER_ID, userId, cookie.getMaxAge(), cookie.getPath(), cookie.getDomain());
        WebUtils.addCookie(response, CoreConstants.AVATAR, avatar, cookie.getMaxAge(), cookie.getPath(), cookie.getDomain());


        return new JwtToken(
                loginName,
                null,
                userId,
                accessToken,
                refreshToken
        );
    }
}
