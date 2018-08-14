package com.duke.framework.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created duke on 2018/7/6
 */
@Configuration
@ConfigurationProperties(prefix = "duke.auth", ignoreUnknownFields = false)
public class AuthProperties {
    /**
     * 登陆失败账户锁定时间，单位；分钟，默认;10分钟
     */
    private Integer loginFailureLockTime = 10;

    private Integer defaultWrongPasswordTimes = 5;

    private Cookie cookie = new Cookie();

    public Integer getLoginFailureLockTime() {
        return loginFailureLockTime;
    }

    public void setLoginFailureLockTime(Integer loginFailureLockTime) {
        this.loginFailureLockTime = loginFailureLockTime;
    }

    public Integer getDefaultWrongPasswordTimes() {
        return defaultWrongPasswordTimes;
    }

    public void setDefaultWrongPasswordTimes(Integer defaultWrongPasswordTimes) {
        this.defaultWrongPasswordTimes = defaultWrongPasswordTimes;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public class Cookie {
        private String path;

        private String domain;

        /**
         * 最大存活时间，默认一小时
         */
        private Integer maxAge = 60 * 60;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public Integer getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Integer maxAge) {
            this.maxAge = maxAge;
        }
    }
}
