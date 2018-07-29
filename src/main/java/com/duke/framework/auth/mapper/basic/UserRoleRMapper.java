package com.duke.framework.auth.mapper.basic;

import com.duke.framework.auth.domain.basic.UserRoleR;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleRMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserRoleR record);

    UserRoleR selectByPrimaryKey(String id);

    List<UserRoleR> selectAll();

    int updateByPrimaryKey(UserRoleR record);
}