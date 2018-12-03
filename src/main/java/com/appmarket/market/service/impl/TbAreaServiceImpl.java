package com.appmarket.market.service.impl;

import com.appmarket.market.bean.TbArea;
import com.appmarket.market.bean.TbAreaExample;
import com.appmarket.market.entity.request.TbAreaQry;
import com.appmarket.market.mapper.TbAreaMapper;
import com.appmarket.market.service.TbAreaService;
import com.appmarket.market.utils.DateUtil;
import com.appmarket.market.utils.ParamsUtils;
import com.kingson.common.utils.ObjectUtil;
import com.kingson.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@Service
public class TbAreaServiceImpl implements TbAreaService {
    private static final Logger logger = LoggerFactory.getLogger(TbAreaServiceImpl.class);
    @Autowired
    TbAreaMapper tbAreaMapper;

    @Override
    public Page<TbArea> queryPageList(TbAreaQry query, Pageable pageable) {
        TbAreaExample tbUserExample = new TbAreaExample();
        //设置查询分页
        com.kingson.common.Plugin.Page page = new com.kingson.common.Plugin.Page();
        if(ObjectUtil.isNotObjectEmpty(pageable)){
            page.setBegin(pageable.getOffset());
            page.setLength(pageable.getPageSize());
            //设置查询排序
            String orderStr = ParamsUtils.orderString(pageable);
            if(StringUtil.isNotBlank(orderStr)){
                tbUserExample.setOrderByClause(ParamsUtils.orderString(pageable));
            }
        }
        else {
            page.setBegin(0);
            page.setLength(20);
        }
        if("Y".equals(query.getIsQueryAll()))
            page.setLength(Integer.MAX_VALUE);
        tbUserExample.setPage(page);
        TbAreaExample.Criteria tbUserCriteria =  tbUserExample.createCriteria();
        if(StringUtil.isNotBlank(query.getName())){
            tbUserCriteria.andNameEqualTo(query.getName());
        }
        if(StringUtil.isNotBlank(query.getNameLike())){
            tbUserCriteria.andNameLike("%" + query.getNameLike() + "%");
        }

        if(StringUtil.isNotBlank(query.getBeginDate())  &&StringUtil.isNotBlank(query.getEndDate())){
            try {
                tbUserCriteria.andCreateTimeBetween(DateUtil.parse(query.getBeginDate()+" 00:00:00","yyyy-MM-dd HH:mm:ss"),(DateUtil.parse(query.getEndDate()+" 23:59:59","yyyy-MM-dd HH:mm:ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<TbArea> userList = tbAreaMapper.selectByExample(tbUserExample);
        //设置总数
        int count = tbAreaMapper.countByExample(tbUserExample);
        return new PageImpl(userList, pageable, count);
    }

    @Override
    public List<TbArea> queryAll() {
        return tbAreaMapper.selectAll();
    }

    @Transactional
    @Override
    public int insertOne(TbArea area) {
        tbAreaMapper.insert(area);
        return 1;
    }

    @Transactional
    @Override
    public int updateOne(TbArea area) {
        tbAreaMapper.update(area);
        return 1;
    }


    @Transactional
    @Override
    public int deleteOne(TbArea area) {
        tbAreaMapper.deleteByPrimaryKey(area.getId());
        return 1;
    }
}
