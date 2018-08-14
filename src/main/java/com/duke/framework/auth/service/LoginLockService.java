package com.duke.framework.auth.service;

import com.duke.framework.auth.AuthProperties;
import com.duke.framework.auth.domain.basic.LoginLock;
import com.duke.framework.auth.mapper.basic.LoginLockMapper;
import com.duke.framework.auth.mapper.extend.LoginLockExtendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created duke on 2018/8/9
 */
@Service
@Transactional
public class LoginLockService {

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private LoginLockMapper loginLockMapper;

    @Autowired
    private LoginLockExtendMapper loginLockExtendMapper;

    /**
     * 根据用户id查找，查看用户是否被锁定
     *
     * @param userId 用户id
     * @return LoginLock
     */
    @Transactional(readOnly = true)
    public LoginLock selectByUserId(String userId) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -authProperties.getLoginFailureLockTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = dateFormat.format(calendar.getTime());
        String endTime = dateFormat.format(date);

        return loginLockExtendMapper.selectByUserId(userId, beginTime, endTime);
    }

    /**
     * 根据用户id删除，解锁
     *
     * @param userId 用户id
     */
    public void deleteByUserId(String userId) {
        loginLockExtendMapper.deleteByUserId(userId);
    }

    /**
     * 新增锁
     *
     * @param loginLock 登陆锁
     */
    public void save(LoginLock loginLock) {
        this.deleteByUserId(loginLock.getUserId());
        loginLockMapper.insert(loginLock);
    }

    public void update(LoginLock loginLock) {
        loginLockMapper.updateByPrimaryKey(loginLock);
    }

}
