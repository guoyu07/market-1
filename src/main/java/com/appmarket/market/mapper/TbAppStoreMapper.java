package com.appmarket.market.mapper;

import com.appmarket.market.bean.TbAppStore;
import com.appmarket.market.bean.TbAppStoreExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbAppStoreMapper {
    int countByExample(TbAppStoreExample example);

    int deleteByExample(TbAppStoreExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbAppStore record);

    int insertSelective(TbAppStore record);

    List<TbAppStore> selectByExample(TbAppStoreExample example);

    TbAppStore selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbAppStore record, @Param("example") TbAppStoreExample example);

    int updateByExample(@Param("record") TbAppStore record, @Param("example") TbAppStoreExample example);

    int updateByPrimaryKeySelective(TbAppStore record);

    int updateByPrimaryKey(TbAppStore record);
}