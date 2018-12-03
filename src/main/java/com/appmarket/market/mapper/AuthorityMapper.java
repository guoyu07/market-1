package com.appmarket.market.mapper;


import com.appmarket.market.entity.TbMenuVo;
import com.appmarket.market.entity.TbRoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/16.
 * Email:chenjingxiong@yunnex.com.
 */
public interface AuthorityMapper {

    List<TbRoleVo> selectRolesByUserId(@Param(value = "userAccount") String userAccount);

    List<Map<String,Object>> selectPermissionsByRoleId(@Param(value = "roleId") Integer roleId);

    List<TbMenuVo> selectMenuByUserId(@Param(value = "userAccount") String userAccount);

}
