package com.appmarket.market.mapper;


import com.appmarket.market.bean.TbOperateLog;
import com.appmarket.market.bean.TbOperateLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbOperateLogMapper {
    int countByExample(TbOperateLogExample example);

    int deleteByExample(TbOperateLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbOperateLog record);

    int insertSelective(TbOperateLog record);

    List<TbOperateLog> selectByExample(TbOperateLogExample example);

    TbOperateLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbOperateLog record, @Param("example") TbOperateLogExample example);

    int updateByExample(@Param("record") TbOperateLog record, @Param("example") TbOperateLogExample example);

    int updateByPrimaryKeySelective(TbOperateLog record);

    int updateByPrimaryKey(TbOperateLog record);
}