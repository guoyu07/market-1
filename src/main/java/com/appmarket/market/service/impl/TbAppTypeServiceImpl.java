package com.appmarket.market.service.impl;

import com.appmarket.market.bean.TbAppType;
import com.appmarket.market.bean.TbAppTypeExample;
import com.appmarket.market.entity.request.TbAppTypeQry;
import com.appmarket.market.mapper.TbAppTypeMapper;
import com.appmarket.market.service.TbAppTypeService;
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
public class TbAppTypeServiceImpl implements TbAppTypeService {
    private static final Logger logger = LoggerFactory.getLogger(TbAppTypeServiceImpl.class);
    @Autowired
    TbAppTypeMapper tbAppTypeMapper;

    @Override
    public Page<TbAppType> queryPageList(TbAppTypeQry query, Pageable pageable) {
        TbAppTypeExample tbUserExample = new TbAppTypeExample();
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
        TbAppTypeExample.Criteria tbUserCriteria =  tbUserExample.createCriteria();
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
        List<TbAppType> userList = tbAppTypeMapper.selectByExample(tbUserExample);
        //设置总数
        int count = tbAppTypeMapper.countByExample(tbUserExample);
        return new PageImpl(userList, pageable, count);
    }

    @Override
    public List<TbAppType> queryAll() {
        return tbAppTypeMapper.selectAll();
    }

    @Transactional
    @Override
    public int insertOne(TbAppType appType) {
        tbAppTypeMapper.insert(appType);
        return 1;
    }

    @Transactional
    @Override
    public int updateOne(TbAppType appType) {
        tbAppTypeMapper.update(appType);
        return 1;
    }

    @Transactional
    @Override
    public int deleteOne(TbAppType appType) {
        tbAppTypeMapper.deleteByPrimaryKey(appType.getId());
        return 1;
    }
}
