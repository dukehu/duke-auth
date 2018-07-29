package com.duke.framework.auth.service;

import com.duke.framework.auth.domain.extend.AuthUserDetails;
import com.duke.framework.auth.mapper.extend.UserExtendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created duke on 2018/7/25
 */
@Service
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private UserExtendMapper userExtendMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<AuthUserDetails> users = userExtendMapper.selectByUserName(username);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("role_admin");
        authorities.add(simpleGrantedAuthority);
        AuthUserDetails authUserDetails = users.get(0);
        authUserDetails.setAuthorities(authorities);
        return authUserDetails;
    }
}
