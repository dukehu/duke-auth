package com.duke.framework.auth.web.controller;

import com.duke.framework.utils.SecurityUtils;
import com.duke.framework.utils.WebUtils;
import com.duke.framework.web.Response;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created duke on 2018/7/25
 */
@RestController
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private TokenEndpoint tokenEndpoint;

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     * @param request  请求
     * @param response 响应
     * @return Map
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response<OAuth2AccessToken> login(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request,
            HttpServletResponse response) throws
            HttpRequestMethodNotSupportedException {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("client_id", "client_2");
        map.put("grant_type", "password");
        map.put("client_secret", "123456");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken("client_2",
                "", Lists.newArrayList());
        ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseEntity =
                tokenEndpoint.postAccessToken(usernamePasswordAuthenticationToken, map);
        OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenResponseEntity.getBody();
        WebUtils.addCookie(response, "access_token", oAuth2AccessToken.getValue());
        return Response.ok(oAuth2AccessToken);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Principal user(Principal user, HttpServletRequest request) {
        SecurityUtils.getCurrentUserInfo();
        System.out.println(WebUtils.extract(request, "user_name"));
        return user;
    }

}
