package com.duke.framework.auth.config.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created duke on 2018/8/9
 * <p>
 * 参考：https://www.jianshu.com/p/ec9b7bc47de9
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("JwtAuthenticationTokenFilter is working");
        filterChain.doFilter(request, response);
    }
}
