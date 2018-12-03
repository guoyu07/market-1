package com.appmarket.market.web.controller.admin;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.appmarket.market.bean.SnKey;
import com.appmarket.market.bean.TbArea;
import com.appmarket.market.bean.TbTerminal;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbAreaQry;
import com.appmarket.market.entity.request.TbTerminalQry;
import com.appmarket.market.entity.request.TbUserQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbAreaService;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.service.TerminalService;
import com.appmarket.market.utils.PropertiesUtil;
import com.appmarket.market.utils.ServletUtils;
import com.appmarket.market.utils.SnXlsView;
import com.appmarket.market.utils.TerminalXlsView;
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
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by kingson.chan on 2017/4/17.
 * Email:chenjingxiong@yunnex.com.
 */
@Controller
@RequestMapping("/terminal")
public class TerminalController {
    private static final Logger logger = LoggerFactory.getLogger(TerminalController.class);
    @Autowired
    TerminalService terminalService;

    @Autowired
    private TbOperatorLogService tbOperatorLogService;

    @Autowired
    private TbUserService tbUserService;

    @Autowired
    private TbAreaService tbAreaService;

    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions(value = "terminalManage:terminalControl:READ")
    public ModelAndView listView() {

        ModelAndView mav = new ModelAndView("terminal/terminalList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");

        TbUserQry usrQry = new TbUserQry();
        usrQry.setIsQueryAll("Y");
        Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
        TbAreaQry areaQry = new TbAreaQry();
        areaQry.setIsQueryAll("Y");
        Page<TbArea>  areas = tbAreaService.queryPageList(areaQry, null);

        mav.addObject("areas",areas.getContent());
        mav.addObject("users",users.getContent());
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @RequiresPermissions(value = "terminalManage:terminalControl:READ")
    public Result list(TbTerminalQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        Integer role = loginUser.getType();
        if(role != 1){//一般角色
            query.setUid(loginUser.getId());

        }
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbTerminal> page = terminalService.queryPageList(query, pageable);
            TbUserQry usrQry = new TbUserQry();
            usrQry.setIsQueryAll("Y");
            Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
            Map<Integer, TbUser> userMap = new HashMap<>();
            for(TbUser user : users.getContent()) {
                userMap.put(user.getId(), user);
            }
            if(null != page && null != page.getContent()) for(TbTerminal terminal : page.getContent()) {
                if(null != terminal.getUid()) {
                    TbUser user = userMap.get(terminal.getUid());
                    if(null != user)
                        terminal.setUserIdName(user.getId() + "-" +user.getAccount());
                    else
                        terminal.setUserIdName(terminal.getUid().toString());
                }
            }
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @RequiresPermissions(value = "terminalManage:terminalControl:CREATE")
    public ModelAndView export(TbTerminalQry query) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        Integer role = loginUser.getType();
        if(role != 1){//一般角色
            query.setUid(loginUser.getId());

        }
        Map<String, Object> model = new HashMap<>();
        Page<TbTerminal> page = terminalService.queryPageList(query, null);
        model.put("tbTerminal", page.getContent());
        TerminalXlsView xlsView = new TerminalXlsView();
        return new ModelAndView(xlsView, model);
    }

    @RequestMapping(value = "preImport", method = RequestMethod.GET)
    @RequiresPermissions(value = "terminalManage:terminalControl:IMPORT")
    public ModelAndView preImport() {
        ModelAndView mav =  new ModelAndView("terminal/terminalImport");
        TbUserQry usrQry = new TbUserQry();
        usrQry.setIsQueryAll("Y");
        Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
        TbAreaQry areaQry = new TbAreaQry();
        areaQry.setIsQueryAll("Y");
        Page<TbArea>  areas = tbAreaService.queryPageList(areaQry, null);

        mav.addObject("areas",areas.getContent());
        mav.addObject("users",users.getContent());
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "fileImport", method = RequestMethod.POST)
    @RequiresPermissions(value = "terminalManage:terminalControl:IMPORT")
    public Result fileImport(String terminalFile, HttpServletRequest request, String area, String uid  ) {
        Result result = new Result(Result.Status.OK, "操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            String realPath = request.getSession().getServletContext().getRealPath("");
            String tmpFilePath = PropertiesUtil.getEntryValue("tmp.pic.path") + loginUser.getName();
            File tmpFile = new File(realPath + tmpFilePath, terminalFile);
            List<TbTerminal> tbTerminalList = this.analysis(tmpFile, loginUser);
            if(!CollectionUtils.isEmpty(tbTerminalList)){
                for (TbTerminal tbTerminal : tbTerminalList) {
//                    if(tbTerminal.getSn().length() != 16){
//                        result.setStatus(Result.Status.ERROR);
//                        result.setMessage("机身号"+tbTerminal.getSn()+"不正确，请输入16位机身号");
//                        return result;
//                    }
                    
//                    TbUser tbUser = tbUserService.queryByUid(tbTerminal.getUid());
//                    if(tbUser == null){
//                        result.setStatus(Result.Status.ERROR);
//                        result.setMessage("机构号"+tbTerminal.getUid() +"不存在");
//                        return result;
//                    }

                    TbTerminal terminal = terminalService.queryById(tbTerminal.getImei());
                    if(terminal != null){
                        result.setStatus(Result.Status.ERROR);
                        result.setMessage("IMEI号"+tbTerminal.getImei() +"已经存在");
                        return result;
                    }
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(uid))
                        tbTerminal.setUid(Integer.parseInt(uid));
                    else {
                        result.setStatus(Result.Status.ERROR);
                        result.setMessage("请选择机构号");
                        return result;
                    }
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(area))
                        tbTerminal.setArea(area);
                }
                terminalService.saveOrUpdateList(tbTerminalList,loginUser);
            }
            

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
            tbOperatorLogService.insertLog(loginUser.getName(), "导入终端:" + result.getStatus() + ";" + JSON.toJSONString(result.getMessage()), terminalFile, ServletUtils.getClientIpAddress(request));
        }
        return result;
    }

    private List<TbTerminal> analysis(File file, TbUser tbUser) throws Exception {
        if (!"xls".equals(FilenameUtils.getExtension(file.getName()))) {
            throw new Exception("对不起，只支持扩展名为xls的文件。");
        }
        Workbook wb;
        Sheet sheet;
        Row row = null;
        Date dt = new Date();
        List<TbTerminal> list = new ArrayList<TbTerminal>();
        try {
            wb = new HSSFWorkbook(FileUtils.openInputStream(file));
            // 获得第一个工作表对象
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            // 正文内容应该从第2行开始
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                TbTerminal tbTerminal = new TbTerminal();
                tbTerminal.setImei(row.getCell(0).getStringCellValue());
                tbTerminal.setSn(row.getCell(1).getStringCellValue());
                if(tbUser.getType() == 1){
                    tbTerminal.setUid(Integer.valueOf(row.getCell(2).getStringCellValue()));
                }
                else {
                    tbTerminal.setUid(tbUser.getId());
                }

                tbTerminal.setShopName(row.getCell(3).getStringCellValue());
                tbTerminal.setShopContacts(row.getCell(4).getStringCellValue());
                tbTerminal.setShopPhone(row.getCell(5).getStringCellValue());
                tbTerminal.setShopLongitude(new BigDecimal(row.getCell(6).getStringCellValue()));
                tbTerminal.setShopLatitude(new BigDecimal(row.getCell(7).getStringCellValue()));
                list.add(tbTerminal);
            }

        } catch (Exception e) {
            logger.error("analysis||sysError:", e);
            throw new Exception("文件格式不正确");
        } finally {
            if(FileUtils.deleteQuietly(file)){
                logger.info("处理成功之后，删除上传上来的文件");
            }
            else {
                logger.info("删除上传上来的文件失败，文件名：" + file);
            }

        }
        return list;
    }

    @RequestMapping(value = "/delete/{imei}", method = RequestMethod.GET)
    @RequiresPermissions(value = "terminalManage:terminalControl:DELETE")
    public String preImport(@PathVariable("imei") String imei) {
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        int result = terminalService.deleteTerminalInfo(imei,loginUser.getId());
        return "redirect:/terminal";

    }


    @RequestMapping(value = "/position" ,method = RequestMethod.GET)
    @RequiresPermissions(value = "terminalManage:terminalPosition:READ")
    public ModelAndView position() {
        ModelAndView mav = new ModelAndView("terminal/position");
        TbUserQry usrQry = new TbUserQry();
        usrQry.setIsQueryAll("Y");
        Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
        TbAreaQry areaQry = new TbAreaQry();
        areaQry.setIsQueryAll("Y");
        Page<TbArea>  areas = tbAreaService.queryPageList(areaQry, null);

        mav.addObject("areas",areas.getContent());
        mav.addObject("users",users.getContent());
        return mav;
    }


    @RequestMapping(value = "{imei}", method = RequestMethod.GET)
    @RequiresPermissions(value = "terminalManage:terminalControl:READ")
    public ModelAndView getUser(@PathVariable("imei") String imei, @PageableDefault Pageable pageable) {
        ModelAndView mav = new ModelAndView("terminal/editTerminal");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        TbTerminal vo = terminalService.queryById(imei);
        TbUserQry usrQry = new TbUserQry();
        usrQry.setIsQueryAll("Y");
        Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
        TbAreaQry areaQry = new TbAreaQry();
        areaQry.setIsQueryAll("Y");
        Page<TbArea>  areas = tbAreaService.queryPageList(areaQry, null);
        mav.addObject("terminal",vo);
        mav.addObject("users",users.getContent());
        mav.addObject("areas",areas.getContent());
        mav.addObject("loginUser",loginUser);
        return mav;
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    @RequiresPermissions(value = "terminalManage:terminalControl:CREATE")
    public Result addOne( TbTerminal tbTerminal, HttpServletRequest req, BindingResult bindingResult) {
        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            if(!StringUtils.isEmpty(tbTerminal.getImei())){
                TbTerminal terminal = terminalService.queryById(tbTerminal.getImei());
                if(null == terminal){
                    if(loginUser.getType() == 2){//一般用户才写入uid
                        tbTerminal.setUid(loginUser.getId());
                    }
                    if(null != tbTerminal.getUid()){
                        TbUser tbUser = tbUserService.queryByUid(tbTerminal.getUid());
                        if(null != tbUser){
                            terminalService.insert(tbTerminal);
                            result.setMessage(tbTerminal);
                        }
                        else {
                            result.setStatus(Result.Status.ERROR);
                            result.setMessage("机构号不存在！");
                        }
                    }else {
                        result.setStatus(Result.Status.ERROR);
                        result.setMessage("机构号不能为空！");
                    }

                }
                else {
                    result.setStatus(Result.Status.ERROR);
                    result.setMessage("IMEI已经存在！");
                }
                
                

            }
            else {
                result.setStatus(Result.Status.ERROR);
                result.setMessage("IMEI不能为空！");
            }

        }
        catch (DuplicateKeyException e) {
            logger.error("添加终端失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("机身号重复");
        }
        catch (Exception e) {
            logger.error("添加终端失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败");
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "添加终端："+result.getStatus(), JSON.toJSONString(result.getMessage()), ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    @RequiresPermissions(value = "terminalManage:terminalControl:UPDATE")
    public Result editOne(TbTerminal tbTerminal,HttpServletRequest req) {

        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            terminalService.update(tbTerminal);
            result.setMessage(tbTerminal);
        } 
        catch (DuplicateKeyException e) {
            logger.error("修改终端失败：",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("机身号已经存在");
        }
        catch (Exception e) {
            logger.error("修改终端失败：",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败");
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "修改终端："+result.getStatus(), JSON.toJSONString(result.getMessage()),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @RequestMapping(value = "/set",method = RequestMethod.GET)
    @RequiresPermissions(value = "terminalManage:terminalControl:READ")
    public ModelAndView setView() {
        ModelAndView mav = new ModelAndView("terminalSet/setList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        return mav;
    }

    @RequestMapping(value = "/set/export", method = RequestMethod.POST)
    @RequiresPermissions(value = "terminalManage:terminalSet:EXPORT")
    public ModelAndView export(@RequestParam("snList") String snList ) throws Exception {
        Map<String, Object> model = new HashMap<>();
        String[] snListString = snList.split(",");
        List<SnKey> snKeyList = terminalService.snKeyList(Arrays.asList(snListString));
        model.put("snKeyList", snKeyList);
        SnXlsView xlsView = new SnXlsView();
        return new ModelAndView(xlsView, model);
    }

}

