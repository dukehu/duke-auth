package com.duke.framework.auth.mapper.basic;

import com.duke.framework.auth.domain.basic.ResourceOperationCodeR;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceOperationCodeRMapper {
    int deleteByPrimaryKey(String id);

    int insert(ResourceOperationCodeR record);

    ResourceOperationCodeR selectByPrimaryKey(String id);

    List<ResourceOperationCodeR> selectAll();

    int updateByPrimaryKey(ResourceOperationCodeR record);
}