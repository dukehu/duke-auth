package com.duke.framework.auth.mapper.extend;

import com.duke.framework.auth.domain.extend.AuthUserDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created duke on 2018/7/25
 */
@Mapper
public interface UserExtendMapper {
    /**
     * 根据用户名查找
     *
     * @param username 用户名
     * @return List<AuthUserDetails>
     */
    List<AuthUserDetails> selectByUserName(@Param("username") String username);
}
