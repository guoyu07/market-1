package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbImeiApp;
import com.appmarket.market.bean.TbImeiAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbImeiAppMapper {
    int countByExample(TbImeiAppExample example);

    int deleteByExample(TbImeiAppExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbImeiApp record);

    int insertSelective(TbImeiApp record);

    List<TbImeiApp> selectByExample(TbImeiAppExample example);

    TbImeiApp selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbImeiApp record, @Param("example") TbImeiAppExample example);

    int updateByExample(@Param("record") TbImeiApp record, @Param("example") TbImeiAppExample example);

    int updateByPrimaryKeySelective(TbImeiApp record);

    int updateByPrimaryKey(TbImeiApp record);
}