package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbUserRole;
import com.appmarket.market.bean.TbUserRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserRoleMapper {
    int countByExample(TbUserRoleExample example);

    int deleteByExample(TbUserRoleExample example);

    int insert(TbUserRole record);

    int insertSelective(TbUserRole record);

    List<TbUserRole> selectByExample(TbUserRoleExample example);

    int updateByExampleSelective(@Param("record") TbUserRole record, @Param("example") TbUserRoleExample example);

    int updateByExample(@Param("record") TbUserRole record, @Param("example") TbUserRoleExample example);
}