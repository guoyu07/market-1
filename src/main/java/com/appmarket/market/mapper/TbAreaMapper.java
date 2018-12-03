package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbArea;
import com.appmarket.market.bean.TbAreaExample;

import java.util.List;

public interface TbAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbArea record);

    int update(TbArea record);

    List<TbArea> selectByExample(TbAreaExample example);

    List<TbArea> selectAll();

    int countByExample(TbAreaExample example);
}