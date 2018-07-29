package com.duke.framework.auth.mapper.basic;

import com.duke.framework.auth.domain.basic.OperationCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationCodeMapper {
    int deleteByPrimaryKey(String id);

    int insert(OperationCode record);

    OperationCode selectByPrimaryKey(String id);

    List<OperationCode> selectAll();

    int updateByPrimaryKey(OperationCode record);
}