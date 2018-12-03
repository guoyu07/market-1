package com.appmarket.market.service;


import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbUserQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public interface TbUserService {
    TbUser queryById(String name);

    TbUser authentication(TbUser user);

    Page<TbUser> queryPageList(TbUserQry query, Pageable pageable);

    int insertUserAndRoles(TbUser user, Integer[] roleIds);

    TbUser updateUserAndRoles(TbUser user, Integer[] roleIds);

    int insertUserList(List<TbUser> tbUserList);

    int updateByIdSelective(TbUser tbUser);

    TbUser queryByUid(Integer uid);

}
