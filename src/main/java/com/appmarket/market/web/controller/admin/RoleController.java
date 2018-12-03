package com.appmarket.market.web.controller.admin;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.appmarket.market.bean.TbRole;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.TbMenuVo;
import com.appmarket.market.entity.request.TbRoleQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbMenuService;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.service.TbRoleService;
import com.appmarket.market.utils.ServletUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private TbMenuService menuService;
    @Autowired
    private TbRoleService roleService;
    @Autowired
    private TbOperatorLogService userOperatorLogService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:roleManage:READ")
    public ModelAndView getRole(@PathVariable("id") Integer id) {
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        TbRole vo = roleService.queryById(id);
        List<TbMenuVo> menus =menuService.selectMenuByUserId(loginUser.getName());
        Integer roleId = vo==null?null:vo.getId();
        String menuData = menuService.getRoleMenuJson(menus,roleId);

        ModelAndView mav = new ModelAndView("sysManage/editRole");
        mav.addObject("menuData",menuData);
        mav.addObject("role",vo);
        return mav;
    }

    @RequiresPermissions(value = "sysManage:roleManage:READ")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listView() {
        return new ModelAndView("sysManage/roleList");
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:roleManage:READ")
    public Result list(TbRoleQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        query.setSysLevel(loginUser.getType());

        Page<TbRole> page = roleService.queryPageList(query, pageable);
        result.setStatus(Result.Status.OK);
        result.setMessage(page);
        logger.info("role list;");
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:roleManage:CREATE")
    public Result addOne(TbRole role,String permissions,HttpServletRequest req) {
        Result result = new Result(Result.Status.OK,"操作成功");
        if(StringUtils.isEmpty(permissions)){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("请设置角色的菜单权限");
            return result;
        }
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        role.setCreateUser(loginUser.getName());
        //系统管理员增加角色
        role.setSysLevel(1);
        /*if(loginUser.getType()==1L){
            if(role.getSysLevel().equals(2L)){
                role.setCreateUser("sys");
            }
        }else if(loginUser.getType()!=1L){
            role.setSysLevel(loginUser.getType());
        }*/

        Map<Integer,TbMenuVo> allMenuMap = menuService.queryMap();
        boolean flag = roleService.insertOrUpdateRoleMenus(role,permissions.split(","),allMenuMap);
        if(!flag){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败");
        }
        userOperatorLogService.insertLog(loginUser.getName(), "添加角色："+result.getStatus(), JSON.toJSONString(role), ServletUtils.getClientIpAddress(req));
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions(value = "sysManage:roleManage:UPDATE")
    public Result editOne(TbRole role,String permissions,HttpServletRequest req) {

        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        if(StringUtils.isEmpty(permissions)){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("请设置角色的菜单权限");
            return result;
        }
        if(role.getId().equals(1L)){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("不能编辑系统管理员组权限");
            return result;
        }
        /*TbRole rvo = roleService.queryById(role.getId());
        boolean editFlag = false;
        String createUser = loginUser.getName();
        if(loginUser.getType()==1L){
            if(role.getSysLevel().equals(1L)){
                role.setCreateUser(createUser);
            }else if(role.getSysLevel().equals(2L)){
                role.setCreateUser("sys");
            }
            if((rvo.getCreateUser().equals(createUser)||rvo.getCreateUser().equals("sys")))
                editFlag=true;
        }else{
            if(rvo.getCreateUser().equals(createUser))
                editFlag=true;
        }
        if(!editFlag){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("对不起，您只能修改自己创建的角色！");
            return result;
        }*/
        Map<Integer,TbMenuVo> allMenuMap = menuService.queryMap();
        boolean flag = roleService.insertOrUpdateRoleMenus(role,permissions.split(","),allMenuMap);
        if(!flag){
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败");
        }
        userOperatorLogService.insertLog(loginUser.getName(), "修改角色："+result.getStatus()+";"+JSON.toJSONString(role), role.toString(),ServletUtils.getClientIpAddress(req));
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "{id}",method = RequestMethod.DELETE)
    @RequiresPermissions(value = "sysManage:roleManage:DELETE")
    public Result delOne(@PathVariable("id") Integer roleId,HttpServletRequest req) {

        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            if(roleId.equals(1L)){
                result.setStatus(Result.Status.ERROR);
                result.setMessage("不能删除系统管理员组");
                return result;
            }
            TbRole rvo = roleService.queryById(roleId);
            String createUser = loginUser.getName();
            if(loginUser.getType()!=1L&&"sys".equals(rvo.getCreateUser())){
                result.setStatus(Result.Status.ERROR);
                result.setMessage("对不起，您不能删除系统角色！");
                return result;
            }
            if(loginUser.getType()!=1L&&!rvo.getCreateUser().equals(createUser)){
                result.setStatus(Result.Status.ERROR);
                result.setMessage("对不起，您只能删除自己创建的角色！");
                return result;
            }
            int num = roleService.deleteById(roleId);
        } catch (Exception e) {
            logger.error("删除角色失败。",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败");
        }finally {
            userOperatorLogService.insertLog(loginUser.getName(), "删除角色 roleId="+roleId, JSON.toJSONString(result.getMessage()),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }
}
