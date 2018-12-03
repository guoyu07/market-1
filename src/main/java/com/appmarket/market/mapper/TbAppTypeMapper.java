package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbAppType;
import com.appmarket.market.bean.TbAppTypeExample;

import java.util.List;

public interface TbAppTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbAppType record);

    int update(TbAppType record);

    List<TbAppType> selectByExample(TbAppTypeExample example);

    List<TbAppType> selectAll();

    int countByExample(TbAppTypeExample example);
}