package com.appmarket.market.service;

import com.appmarket.market.bean.TbAppType;
import com.appmarket.market.entity.request.TbAppTypeQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TbAppTypeService {

    /**
     *  查询
     * @param query
     * @param pageable
     * @return
     */
    Page<TbAppType> queryPageList(TbAppTypeQry query, Pageable pageable);

    /**
     *  查询所有
     * @return
     */
    List<TbAppType> queryAll();

    /**
     * 新增
     * @param area
     * @return
     */
    int insertOne(TbAppType area);

    /**
     * 编辑
     * @param area
     * @return
     */
    int updateOne(TbAppType area);

    /**
     * 删除
     * @param area
     * @return
     */
    int deleteOne(TbAppType area);
}
