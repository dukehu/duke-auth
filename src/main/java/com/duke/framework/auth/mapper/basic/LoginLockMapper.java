package com.duke.framework.auth.mapper.basic;

import com.duke.framework.auth.domain.basic.LoginLock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginLockMapper {
    int deleteByPrimaryKey(String id);

    int insert(LoginLock record);

    LoginLock selectByPrimaryKey(String id);

    List<LoginLock> selectAll();

    int updateByPrimaryKey(LoginLock record);
}