package com.duke.framework.auth.config;

import com.duke.framework.auth.AuthProperties;
import com.duke.framework.auth.config.jwt.JwtToken;
import com.duke.framework.auth.domain.basic.LoginLock;
import com.duke.framework.auth.domain.extend.AuthUserDetails;
import com.duke.framework.auth.service.AuthUserDetailService;
import com.duke.framework.auth.service.LoginLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * Created duke on 2018/8/7
 */
public class AuthAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthAuthenticationProvider.class);
    private final AuthUserDetailService authUserDetailService;
    private final LoginLockService loginLockService;
    private PasswordEncoder passwordEncoder;
    private AuthProperties authProperties;

    public AuthAuthenticationProvider(AuthProperties authProperties,
                                      PasswordEncoder passwordEncoder,
                                      AuthUserDetailService authUserDetailService,
                                      LoginLockService loginLockService) {
        this.authProperties = authProperties;
        this.passwordEncoder = passwordEncoder;
        this.authUserDetailService = authUserDetailService;
        this.loginLockService = loginLockService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    (UsernamePasswordAuthenticationToken) authentication;

            Object credentials = usernamePasswordAuthenticationToken.getCredentials();
            String username = usernamePasswordAuthenticationToken.getName();

            if (ObjectUtils.isEmpty(username)) {
                // todo 抛异常，用户名不能为空
            }

            if (ObjectUtils.isEmpty(credentials)) {
                // todo 抛异常，密码不能为空
            }

            AuthUserDetails authUserDetails = authUserDetailService.loadUserByUsername(username);

            if (ObjectUtils.isEmpty(authUserDetails)) {
                // todo 抛异常，用户不存在
            }


            check(authentication, authUserDetails);

            JwtToken jwtToken = new JwtToken(
                    username,
                    null,
                    Collections.emptyList(),
                    authUserDetails.getUserId(),
                    null,
                    null
            );
            Map<String, Object> additionalInformation = buildAdditionalInformation(authUserDetails);
            jwtToken.setAdditionalInformation(additionalInformation);

            jwtToken.setDetails(authUserDetails);
            return jwtToken;
        }
        return null;
    }

    /**
     * 构造附件信息
     *
     * @param authUserDetails 用户详情
     * @return map
     */
    private Map<String, Object> buildAdditionalInformation(AuthUserDetails authUserDetails) {
        Map<String, Object> additionalInformation = new HashMap<>();
        String realName = authUserDetails.getRealName();
        String nickName = authUserDetails.getNickName();
        String avatar = authUserDetails.getAvatar();
        Integer status = authUserDetails.getUserStatus();
        String userId = authUserDetails.getUserId();

        if (!ObjectUtils.isEmpty(realName)) {
            additionalInformation.put("realName", realName);
        }
        if (!ObjectUtils.isEmpty(nickName)) {
            additionalInformation.put("nickName", nickName);
        }
        if (!ObjectUtils.isEmpty(avatar)) {
            additionalInformation.put("avatar", avatar);
        }
        if (!ObjectUtils.isEmpty(status)) {
            additionalInformation.put("status", status);
        }
        if (!ObjectUtils.isEmpty(userId)) {
            additionalInformation.put("userId", userId);
        }
        return additionalInformation;
    }

    /**
     * 类似AbstractUserDetailsAuthenticationProvider里面的对user的校验，这块加上了自己的一些校验
     *
     * @param authentication  authentication
     * @param authUserDetails authUserDetails
     */
    private void check(Authentication authentication, AuthUserDetails authUserDetails) {
        String userId = authUserDetails.getUserId();
        String username = authUserDetails.getLoginName();
        String password = (String) authentication.getCredentials();

        // todo，首先判断一下用户的状态


        // 查询当前用户有没有输错过密码
        LoginLock loginLock = loginLockService.selectByUserId(userId);

        if (!ObjectUtils.isEmpty(loginLock)
                && authProperties.getDefaultWrongPasswordTimes().equals(loginLock.getLockNum())) {
            // todo，抛出异常，账户被锁定，请联系管理员，或者是请耐心等待1小时
        }

        if (!passwordEncoder.matches(password, authUserDetails.getLoginPassword())) {
            // 密码错误
            if (ObjectUtils.isEmpty(loginLock)) {
                // 之前没有输错过密码，新增一条数据
                loginLock = new LoginLock();
                loginLock.setId(UUID.randomUUID().toString());
                loginLock.setLockDate(new Date());
                loginLock.setLockNum(1);
                loginLock.setUserId(userId);
                loginLockService.save(loginLock);
            } else if (loginLock.getLockNum() < authProperties.getDefaultWrongPasswordTimes()) {

            }
        }

        loginLockService.deleteByUserId(userId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication)
                || UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || JwtToken.class.isAssignableFrom(authentication);
    }
}
