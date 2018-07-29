package com.duke.framework.auth.mapper.basic;

import com.duke.framework.auth.domain.basic.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceMapper {
    int deleteByPrimaryKey(String id);

    int insert(Resource record);

    Resource selectByPrimaryKey(String id);

    List<Resource> selectAll();

    int updateByPrimaryKey(Resource record);
}