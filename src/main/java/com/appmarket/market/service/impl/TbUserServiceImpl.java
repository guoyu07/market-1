package com.appmarket.market.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.appmarket.market.bean.*;
import com.appmarket.market.entity.request.TbUserQry;
import com.appmarket.market.mapper.TbUserMapper;
import com.appmarket.market.mapper.TbUserRoleMapper;
import com.appmarket.market.service.TbRoleService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.utils.DateUtil;
import com.appmarket.market.utils.ParamsUtils;
import com.appmarket.market.utils.SecurityHelper;
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
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
@Service
public class TbUserServiceImpl implements TbUserService {
    private static final Logger logger = LoggerFactory.getLogger(TbUserServiceImpl.class);
    @Autowired
    TbUserMapper tbUserMapper;
    
    @Autowired
    TbUserRoleMapper tbUserRoleMapper;

    @Autowired
    private TbRoleService tbRoleService;

    @Override
    public TbUser queryById(String name) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria tbUserCriteria =  tbUserExample.createCriteria();
        tbUserCriteria.andNameEqualTo(name);
        List<TbUser> userList = tbUserMapper.selectByExample(tbUserExample);
        if (!CollectionUtils.isEmpty(userList)) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public TbUser authentication(TbUser user) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria tbUserCriteria =  tbUserExample.createCriteria();
        tbUserCriteria.andNameEqualTo(user.getName());
        tbUserCriteria.andPasswordEqualTo(user.getPassword());
        List<TbUser> userList = tbUserMapper.selectByExample(tbUserExample);
        if (!CollectionUtils.isEmpty(userList)) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public Page<TbUser> queryPageList(TbUserQry query, Pageable pageable) {
        TbUserExample tbUserExample = new TbUserExample();
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
        if("Y".equals(query.getIsQueryAll())) {
            page.setLength(Integer.MAX_VALUE);
        }
        tbUserExample.setPage(page);
        TbUserExample.Criteria tbUserCriteria =  tbUserExample.createCriteria();
        if(StringUtil.isNotBlank(query.getName())){
            tbUserCriteria.andNameEqualTo(query.getName());
        }
        if(StringUtil.isNotBlank(query.getNameLike())){
            tbUserCriteria.andNameLike("%" + query.getNameLike() + "%");
        }
        if(StringUtil.isNotBlank(query.getStatus())){
            tbUserCriteria.andStatusEqualTo(query.getStatus());
        }
        if(null != query.getId()){
            tbUserCriteria.andIdEqualTo(query.getId());
        }
        if(StringUtil.isNotBlank(query.getBeginDate())  &&StringUtil.isNotBlank(query.getEndDate())){
            try {
                tbUserCriteria.andCreateTimeBetween(DateUtil.parse(query.getBeginDate()+" 00:00:00","yyyy-MM-dd HH:mm:ss"),(DateUtil.parse(query.getEndDate()+" 23:59:59","yyyy-MM-dd HH:mm:ss")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<TbUser> userList = tbUserMapper.selectByExample(tbUserExample);
        //设置总数
        int count = tbUserMapper.countByExample(tbUserExample);
        return new PageImpl(userList, pageable, count);
    }

    @Transactional
    @Override
    public int insertUserAndRoles(TbUser user, Integer[] roleIds) {
        for (Integer roleId : roleIds) {
            TbUserRole tbUserRole = new TbUserRole();
            tbUserRole.setRoleId(roleId);
            tbUserRole.setUserAccount(user.getName());
            tbUserRoleMapper.insertSelective(tbUserRole);
        }
        user.setPassword(SecurityHelper.getMd5Hex(user.getPassword().getBytes()));
        return  tbUserMapper.insertSelective(user);
    }

    @Transactional
    @Override
    public TbUser updateUserAndRoles(TbUser user, Integer[] roleIds) {
        if(StringUtils.isEmpty(user.getPassword())){
            user.setPassword(null);
        }
        TbUser tbUserOld = tbUserMapper.selectByPrimaryKey(user.getId());
        if(null != roleIds && roleIds.length > 0){
            TbUserRoleExample tbUserRoleExample = new TbUserRoleExample();
            TbUserRoleExample.Criteria tbUserRolCriteria = tbUserRoleExample.createCriteria();
            tbUserRolCriteria.andUserAccountEqualTo(tbUserOld.getName());
            tbUserRoleMapper.deleteByExample(tbUserRoleExample);
            user.setType(2);

            for (Integer roleId : roleIds) {
                TbUserRole tbUserRole = new TbUserRole();
                tbUserRole.setUserAccount(user.getName());
                tbUserRole.setRoleId(roleId);
                tbUserRoleMapper.insertSelective(tbUserRole);
                TbRole tbRole = tbRoleService.queryById(roleId);
                if(tbRole.getId() == 1){
                    user.setType(1);//修改为超级用户
                }
            }
        }
        tbUserMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    @Transactional
    @Override
    public int insertUserList(List<TbUser> tbUserList) {
        logger.info("批量导入数据");
        for (TbUser tbUser : tbUserList) {
            tbUserMapper.insertSelective(tbUser);
        }
        return 1;
    }

    @Override
    public int updateByIdSelective(TbUser tbUser) {
        return tbUserMapper.updateByPrimaryKeySelective(tbUser);
    }

    @Override
    public TbUser queryByUid(Integer uid) {
        return tbUserMapper.selectByPrimaryKey(uid);
    }
}
