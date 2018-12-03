package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbSysAppStore;
import com.appmarket.market.bean.TbSysAppStoreExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbSysAppStoreMapper {
    int countByExample(TbSysAppStoreExample example);

    int deleteByExample(TbSysAppStoreExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbSysAppStore record);

    int insertSelective(TbSysAppStore record);

    List<TbSysAppStore> selectByExample(TbSysAppStoreExample example);

    TbSysAppStore selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbSysAppStore record, @Param("example") TbSysAppStoreExample example);

    int updateByExample(@Param("record") TbSysAppStore record, @Param("example") TbSysAppStoreExample example);

    int updateByPrimaryKeySelective(TbSysAppStore record);

    int updateByPrimaryKey(TbSysAppStore record);
}