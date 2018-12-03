package com.appmarket.market.service;

import com.appmarket.market.bean.TbArea;
import com.appmarket.market.entity.request.TbAreaQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TbAreaService {

    /**
     *  查询
     * @param query
     * @param pageable
     * @return
     */
    Page<TbArea> queryPageList(TbAreaQry query, Pageable pageable);

    /**
     *  查询所有
     * @return
     */
    List<TbArea> queryAll();

    /**
     * 新增
     * @param area
     * @return
     */
    int insertOne(TbArea area);

    /**
     * 更新
     * @param area
     * @return
     */
    int updateOne(TbArea area);

    /**
     * 删除
     * @param area
     * @return
     */
    int deleteOne(TbArea area);
}
