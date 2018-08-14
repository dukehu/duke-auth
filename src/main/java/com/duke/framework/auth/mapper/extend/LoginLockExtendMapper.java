package com.duke.framework.auth.mapper.extend;

import com.duke.framework.auth.domain.basic.LoginLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created duke on 2018/8/9
 */
@Mapper
public interface LoginLockExtendMapper {

    /**
     * 根据用户id查找
     *
     * @param userId    用户id
     * @param beginTime 开始时间，当前时间-登陆锁定时间
     * @param endTime   当前时间
     * @return LoginLock
     */
    LoginLock selectByUserId(@Param("userId") String userId,
                             @Param("beginTime") String beginTime,
                             @Param("endTime") String endTime);

    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     */
    void deleteByUserId(@Param("userId") String userId);
}
