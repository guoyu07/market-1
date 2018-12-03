package com.appmarket.market.service;


import com.appmarket.market.entity.TbMenuVo;

import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public interface TbMenuService {

    List<Map<String,Object>> selectPermissionsByRoleId(Integer roleId);

    List<TbMenuVo> selectMenuByUserId(String userAccount);

    String getRoleMenuJson(List<TbMenuVo> baseMenu, Integer roleId);

    Map<Integer,TbMenuVo> queryMap();
}
