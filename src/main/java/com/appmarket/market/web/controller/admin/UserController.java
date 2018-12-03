package com.appmarket.market.web.controller.admin;


import com.alibaba.fastjson.JSON;
import com.appmarket.market.bean.TbRole;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.TbRoleVo;
import com.appmarket.market.entity.request.TbUserQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.service.TbRoleService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.utils.PropertiesUtil;
import com.appmarket.market.utils.SecurityHelper;
import com.appmarket.market.utils.ServletUtils;
import com.appmarket.market.utils.XlsView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private TbUserService sysUserService;
    @Autowired
    private TbRoleService tbRoleService;
    @Autowired
    private TbOperatorLogService tbOperatorLogService;



    @RequestMapping(value = "{account}", method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:userManage:READ")
    public ModelAndView getUser(@PathVariable("account") String account) {
        ModelAndView mav = new ModelAndView("sysManage/editUser");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        TbUser vo = sysUserService.queryById(account);
        TbRole tbRoleQry = new TbRole();
        tbRoleQry.setSysLevel(1);
        tbRoleQry.setCreateUser("admin");
        List<TbRole> roleList = tbRoleService.queryList(tbRoleQry);

        List<TbRoleVo> roleVos = new ArrayList<>();
        for (TbRole tbRole : roleList) {
            TbRoleVo tbRoleVo = new  TbRoleVo();
            org.springframework.beans.BeanUtils.copyProperties(tbRole, tbRoleVo);
            roleVos.add(tbRoleVo);

        }

        if(vo != null){
            //表示修改某个用户，取用户的角色
            List<TbRoleVo> userRoles = tbRoleService.getRolesByUserId(vo.getName());
            List<Integer> roleIds = new ArrayList<Integer>();
            for (TbRoleVo role : userRoles) {
                roleIds.add(role.getId());
            }
            //循环所有角色，设置用户是否拥有该角色权限
            for (TbRoleVo role : roleVos) {
                role.setOwner(roleIds.contains(role.getId()));
            }
        }
        mav.addObject("user",vo);
        mav.addObject("roles", roleVos);
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:userManage:READ")
    public ModelAndView listView() {
        ModelAndView mav = new ModelAndView("sysManage/userList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:userManage:READ")
    public Result list(TbUserQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbUser> page = sysUserService.queryPageList(query, pageable);
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:userManage:CREATE")
    public Result addOne(TbUser user, Integer[] roleIds, HttpServletRequest req) {
        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            user.setType(2);//添加一般用户
            for (Integer roleId : roleIds) {
                TbRole tbRole = tbRoleService.queryById(roleId);
                if(tbRole.getId() == 1){
                    user.setType(1);//添加超级用户
                }
            }
            
            TbUser vo = sysUserService.queryById(user.getName());
            if(vo!=null){
                result.setStatus(Result.Status.ERROR);
                result.setMessage("账号："+user.getName()+"已经被使用，请修改后重新提交！");
                return result;
            }
            sysUserService.insertUserAndRoles(user, roleIds);
        } catch (Exception e) {
            logger.error("添加用户失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败,"+e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "添加系统用户："+result.getStatus(), JSON.toJSONString(user),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions(value = "sysManage:userManage:UPDATE")
    public Result editOne(TbUser user,Integer[] roleIds,HttpServletRequest req) {

        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            if(!StringUtils.isEmpty(user.getPassword()))
                user.setPassword(SecurityHelper.getMd5Hex(user.getPassword().getBytes()));
            TbUser vo = sysUserService.updateUserAndRoles(user, roleIds);
        } catch (Exception e) {
            logger.error("error",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败,"+e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "修改系统用户："+result.getStatus(), JSON.toJSONString(user),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @RequestMapping(value = "preImport", method = RequestMethod.GET)
    @RequiresPermissions(value = "sysManage:userManage:CREATE")
    public ModelAndView preImport() {
        return new ModelAndView("sysManage/userImport");
    }

    @ResponseBody
    @RequestMapping(value = "fileImport", method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:userManage:CREATE")
    public Result fileImport(String userFile, HttpServletRequest request) {
        Result result = new Result(Result.Status.OK, "操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            String realPath = request.getSession().getServletContext().getRealPath("");
            String tmpFilePath = PropertiesUtil.getEntryValue("tmp.pic.path") + loginUser.getName();
            File tmpFile = new File(realPath + tmpFilePath, userFile);
            List<TbUser> list = this.analysis(tmpFile, loginUser.getName());
            this.sysUserService.insertUserList(list);
        }
        catch (DuplicateKeyException e){
            logger.error("fileImport||sysError:", e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败，注册名称冲突！");

        }
        catch (Exception e) {
            logger.error("fileImport||sysError:", e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败," + e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "导入用户:" + result.getStatus() + ";" + JSON.toJSONString(result.getMessage()), userFile, ServletUtils.getClientIpAddress(request));
        }
        return result;
    }

    private List<TbUser> analysis(File file, String oper) throws Exception {
        if (!"xls".equals(FilenameUtils.getExtension(file.getName()))) {
            throw new Exception("对不起，只支持扩展名为xls的文件。");
        }
        Workbook wb;
        Sheet sheet;
        Row row = null;
        Date dt = new Date();
        List<TbUser> list = new ArrayList<TbUser>();
        try {
            wb = new HSSFWorkbook(FileUtils.openInputStream(file));
            // 获得第一个工作表对象
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            // 正文内容应该从第2行开始
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                TbUser tbUser = new TbUser();
                tbUser.setName(row.getCell(0).getStringCellValue());
                tbUser.setPassword(SecurityHelper.getMd5Hex(row.getCell(1).getStringCellValue().getBytes()));
                tbUser.setAccount(row.getCell(2).getStringCellValue());
                tbUser.setStatus(row.getCell(3).getStringCellValue());
                tbUser.setType(Integer.valueOf(row.getCell(4).getStringCellValue()));
                list.add(tbUser);
            }

        } catch (Exception e) {
            logger.error("analysis||sysError:", e);
            throw new Exception("文件格式不正确");
        } finally {
            FileUtils.deleteQuietly(file);//处理成功之后，删除上传上来的文件
        }
        return list;
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @RequiresPermissions(value = "sysManage:userManage:CREATE")
    public ModelAndView export(TbUserQry query) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        Map<String, Object> model = new HashMap<>();
        Page<TbUser> page = sysUserService.queryPageList(query, null);
        model.put("tbUser", page.getContent());
        XlsView xlsView = new XlsView();
        return new ModelAndView(xlsView, model);
    }

}
