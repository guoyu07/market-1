package com.appmarket.market.service;


import com.appmarket.market.bean.TbRole;
import com.appmarket.market.entity.TbMenuVo;
import com.appmarket.market.entity.TbRoleVo;
import com.appmarket.market.entity.request.TbRoleQry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public interface TbRoleService {
    List<TbRoleVo> getRolesByUserId(String name);

    List<TbRole> queryList(TbRole tbRoleQry);

    Page<TbRole> queryPageList(TbRoleQry query, Pageable pageable);

    TbRole queryById(int roleId);

    public boolean insertOrUpdateRoleMenus(TbRole role, String[] arrPermission, Map<Integer, TbMenuVo> allMenuMap);

    int deleteById(Integer roleId);

}
