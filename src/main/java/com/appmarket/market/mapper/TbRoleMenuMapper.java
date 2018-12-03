package com.appmarket.market.mapper;


import com.appmarket.market.bean.TbRoleMenu;
import com.appmarket.market.bean.TbRoleMenuExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbRoleMenuMapper {
    int countByExample(TbRoleMenuExample example);

    int deleteByExample(TbRoleMenuExample example);

    int insert(TbRoleMenu record);

    int insertSelective(TbRoleMenu record);

    List<TbRoleMenu> selectByExample(TbRoleMenuExample example);

    int updateByExampleSelective(@Param("record") TbRoleMenu record, @Param("example") TbRoleMenuExample example);

    int updateByExample(@Param("record") TbRoleMenu record, @Param("example") TbRoleMenuExample example);
}