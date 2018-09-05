package com.duke.framework.auth.web.controller;

import com.duke.framework.CoreConstants;
import com.duke.framework.auth.config.jwt.JwtToken;
import com.duke.framework.auth.config.jwt.JwtTokenProvider;
import com.duke.framework.auth.domain.extend.AuthUserDetails;
import com.duke.framework.utils.WebUtils;
import com.duke.framework.web.Response;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created duke on 2018/7/25
 */
@RestController
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private DefaultTokenServices defaultTokenServices;

    /**
     * 退出登陆
     *
     * @return String
     */
    @RequestMapping(value = "/sign_out", method = RequestMethod.POST)
    public Response<String> logout(HttpServletRequest request,
                                   HttpServletResponse response) {
        WebUtils.clear(request, response);
        String accessToken = WebUtils.extract(request, "access_token");
        defaultTokenServices.revokeToken(accessToken);
        WebUtils.remove(response, request,
                CoreConstants.ACCESS_TOKEN,
                CoreConstants.REFRESH_TOKEN,
                CoreConstants.AVATAR,
                CoreConstants.LOGIN_NAME,
                CoreConstants.USER_ID);
        return Response.ok();
    }

    /**
     * 用户注册
     *
     * @return String
     */
    @RequestMapping(value = "/sign_up", method = RequestMethod.POST)
    public Response<String> signUp() {
        return Response.ok();
    }

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     * @param request  请求
     * @param response 响应
     * @return Map
     */
    @RequestMapping(value = "/sign_in", method = RequestMethod.POST)
    public Response<JwtToken> signIn(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request,
            HttpServletResponse response) throws
            HttpRequestMethodNotSupportedException {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("client_id", "duke_app");
        map.put("grant_type", "password");
        map.put("client_secret", "12345678");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken("duke_app",
                "", Lists.newArrayList());
        ResponseEntity<OAuth2AccessToken> oAuth2AccessTokenResponseEntity =
                tokenEndpoint.postAccessToken(usernamePasswordAuthenticationToken, map);
        OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenResponseEntity.getBody();
        JwtToken jwtToken = jwtTokenProvider.createJwtToken(request, response, oAuth2AccessToken);
        return Response.ok(jwtToken);
    }

    /**
     * 网关过来的时候不需要知道当前登陆用户有哪些操作码，
     * 各个服务过来的时候就得知道有哪些操作码了
     *
     * @param serviceId            服务id
     * @param oAuth2Authentication oAuth2Authentication
     * @param request              请求
     * @return Map
     */
    @RequestMapping(method = {RequestMethod.GET}, value = {
            "/user", "/user/{serviceId}"})
    public Map<String, Object> user(
            @PathVariable(value = "serviceId", required = false) String serviceId,
            OAuth2Authentication oAuth2Authentication,
            HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", "1234");
        Authentication authentication = oAuth2Authentication.getUserAuthentication();
        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getDetails();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            map.put("user", authentication);
            Integer superman = authUserDetails.getSuperman();
            if (1 == superman) {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                SimpleGrantedAuthority simpleGrantedAuthority3 = new SimpleGrantedAuthority("admin");
                authorities.add(simpleGrantedAuthority3);
                map.put("authorities", authorities);
            } else {
                if (!ObjectUtils.isEmpty(serviceId)) {

                }
            }
        }
        return map;
    }
}
