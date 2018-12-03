package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbTerminal;
import com.appmarket.market.bean.TbTerminalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbTerminalMapper {
    int countByExample(TbTerminalExample example);

    int deleteByExample(TbTerminalExample example);

    int deleteByPrimaryKey(String imei);

    int insert(TbTerminal record);

    int insertSelective(TbTerminal record);

    List<TbTerminal> selectByExample(TbTerminalExample example);

    TbTerminal selectByPrimaryKey(String imei);

    int updateByExampleSelective(@Param("record") TbTerminal record, @Param("example") TbTerminalExample example);

    int updateByExample(@Param("record") TbTerminal record, @Param("example") TbTerminalExample example);

    int updateByPrimaryKeySelective(TbTerminal record);

    int updateByPrimaryKey(TbTerminal record);
}