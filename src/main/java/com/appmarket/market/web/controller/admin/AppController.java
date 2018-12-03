package com.appmarket.market.web.controller.admin;

import com.alibaba.fastjson.JSON;
import com.appmarket.market.bean.TbAppStore;
import com.appmarket.market.bean.TbAppType;
import com.appmarket.market.bean.TbSysAppStore;
import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.request.TbAppStoreQry;
import com.appmarket.market.entity.request.TbSysAppStoreQry;
import com.appmarket.market.entity.request.TbUserQry;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.AppService;
import com.appmarket.market.service.TbAppTypeService;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.service.TbUserService;
import com.appmarket.market.utils.AnalysisApk;
import com.appmarket.market.utils.PropertiesUtil;
import com.appmarket.market.utils.ServletUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kingson.chan on 2017/4/18.
 * Email:chenjingxiong@yunnex.com.
 */
@Controller
@RequestMapping("/app")
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);


    @Autowired
    private TbOperatorLogService tbOperatorLogService;

    @Autowired
    private AppService appService;

    @Autowired
    private TbAppTypeService tbAppTypeService;

    @Autowired
    private TbUserService tbUserService;


    @RequestMapping(method = RequestMethod.GET)
    @RequiresPermissions(value = "appManage:appControl:READ")
    public ModelAndView listView() {
        ModelAndView mav = new ModelAndView("app/appList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        List<TbAppType> appTypes = tbAppTypeService.queryAll();
        TbUserQry usrQry = new TbUserQry();
        usrQry.setIsQueryAll("Y");
        Page<TbUser>  users = tbUserService.queryPageList(usrQry,null);
        mav.addObject("appTypes",appTypes);
        mav.addObject("users",users.getContent());
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @RequiresPermissions(value = "appManage:appControl:READ")
    public Result list(TbAppStoreQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        Integer role = loginUser.getType();
        if(role != 1){//一般角色
            query.setUid(loginUser.getId());
        }
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbAppStore> page = appService.queryPageList(query, pageable);
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "preImport", method = RequestMethod.GET)
    @RequiresPermissions(value = "appManage:appControl:IMPORT")
    public ModelAndView preImport() {
        return new ModelAndView("app/appImport");
    }

    @ResponseBody
    @RequestMapping(value = "fileImport", method = RequestMethod.POST)
    @RequiresPermissions(value = "appManage:appControl:IMPORT")
    public Result fileImport(String appFile, HttpServletRequest request) {
        Result result = new Result(Result.Status.OK, "操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            String realPath = request.getSession().getServletContext().getRealPath("");
            String tmpFilePath = PropertiesUtil.getEntryValue("tmp.pic.path") + loginUser.getName();
            File tmpFile = new File(realPath + tmpFilePath, appFile);
            List<TbAppStore> tbAppStoreList = this.analysis(tmpFile, loginUser);
            appService.saveOrUpdateList(tbAppStoreList,loginUser);

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
            tbOperatorLogService.insertLog(loginUser.getName(), "导入终端:" + result.getStatus() + ";" + JSON.toJSONString(result.getMessage()), appFile, request.getRemoteAddr());
        }
        return result;
    }

    private List<TbAppStore> analysis(File file, TbUser tbUser) throws Exception {
        if (!"xls".equals(FilenameUtils.getExtension(file.getName()))) {
            throw new Exception("对不起，只支持扩展名为xls的文件。");
        }
        Workbook wb;
        Sheet sheet;
        Row row = null;
        Date dt = new Date();
        List<TbAppStore> list = new ArrayList<TbAppStore>();
        try {
            wb = new HSSFWorkbook(FileUtils.openInputStream(file));
            // 获得第一个工作表对象
            sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            // 正文内容应该从第2行开始
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                TbAppStore tbAppStore = new TbAppStore();
                tbAppStore.setAppName(row.getCell(0).getStringCellValue());
                tbAppStore.setDescription(row.getCell(1).getStringCellValue());
                tbAppStore.setAppVersionName(row.getCell(2).getStringCellValue());
                tbAppStore.setPackageName(row.getCell(3).getStringCellValue());
                tbAppStore.setUid(tbUser.getId());
                list.add(tbAppStore);
            }

        } catch (Exception e) {
            logger.error("analysis||sysError:", e);
            throw new Exception("文件格式不正确");
        } finally {
            FileUtils.deleteQuietly(file);//处理成功之后，删除上传上来的文件
        }
        return list;
    }

    @RequestMapping(value = "/delete/{appId}", method = RequestMethod.GET)
    //@RequiresPermissions(value = "appManage:appControl:CREATE")
    public String delete(@PathVariable("appId") Integer appId) {
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        int result = appService.deleteAppInfo(appId,loginUser.getId());
        return "redirect:/app";

    }

    @RequestMapping(value = "{appId}", method = RequestMethod.GET)
    //@RequiresPermissions(value = "appManage:appControl:READ")
    public ModelAndView queryById(@PathVariable("appId") Integer appId) {
        ModelAndView mav = new ModelAndView("app/editApp");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        TbAppStore vo = appService.queryById(appId);
        List<TbAppType> appTypes = tbAppTypeService.queryAll();
        mav.addObject("appTypes",appTypes);
        mav.addObject("app",vo);
        return mav;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    //@RequiresPermissions(value = "appManage:appControl:CREATE")
    public Result addOne(TbAppStore tbAppStore,HttpServletRequest req) {
        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            String realPath = req.getSession().getServletContext().getRealPath("");
            String apkFilePath = PropertiesUtil.getEntryValue("tmp.apk.path") + loginUser.getName();
            String pngFilePath = PropertiesUtil.getEntryValue("tmp.png.path") + loginUser.getName();
            String [] fileName = tbAppStore.getAppLocation().split("/");
            File apkfile = new File( apkFilePath, fileName[fileName.length-1]);
            String[] e = AnalysisApk.unZip(apkfile);
            tbAppStore.setAppVersionName(e[0]);
            tbAppStore.setPackageName(e[1]);
            tbAppStore.setAppVersionCode(e[2]);
            tbAppStore.setFileSize(String.valueOf(apkfile.length()));
            tbAppStore.setUid(loginUser.getId());
            /*tbAppStore.setAppLocation(realPath + apkFilePath + tbAppStore.getAppLocation());
            tbAppStore.setIconLocation(realPath+pngFilePath+tbAppStore.getIconLocation());*/
            TbAppStore appStore = appService.querySysAppByPackageName(tbAppStore,loginUser);
            if(appStore == null){
                appService.insertApp(tbAppStore);
            }
            else {
                result.setStatus(Result.Status.ERROR);
                result.setMessage("应用已经存在");
            }
            
        } catch (Exception e) {
            logger.error("添加应用失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败,"+e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "添加应用："+result.getStatus(), JSON.toJSONString(tbAppStore),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    //@RequiresPermissions(value = "appManage:appControl:UPDATE")
    public Result editOne(TbAppStore tbAppStore,Integer[] roleIds,HttpServletRequest req) {

        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            String realPath = req.getSession().getServletContext().getRealPath("");
            String apkFilePath = PropertiesUtil.getEntryValue("tmp.apk.path") + loginUser.getName();
            String pngFilePath = PropertiesUtil.getEntryValue("tmp.png.path") + loginUser.getName();
            String [] fileName = tbAppStore.getAppLocation().split("/");
            File apkfile = new File( apkFilePath, fileName[fileName.length-1]);
            String[] e = AnalysisApk.unZip(apkfile);
            tbAppStore.setAppVersionName(e[0]);
            tbAppStore.setPackageName(e[1]);
            tbAppStore.setAppVersionCode(e[2]);
            tbAppStore.setFileSize(String.valueOf(apkfile.length()));
            TbAppStore appStore = appService.querySysAppByPackageName(tbAppStore,loginUser);
            if(appStore == null){
                appService.updateApp(tbAppStore);
            }
            else {
                result.setStatus(Result.Status.ERROR);
                result.setMessage("应用已经存在");
            }
           
        } catch (Exception e) {
            logger.error("编辑应用失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败,"+e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "更新应用："+result.getStatus(), JSON.toJSONString(tbAppStore),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @RequestMapping(value = "/whitelist",method = RequestMethod.GET)
    //@RequiresPermissions(value = "appManage:appControl:READ")
    public ModelAndView whiteListView() {
        ModelAndView mav = new ModelAndView("app/whiteList");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/whiteList/list",method = RequestMethod.POST)
    //@RequiresPermissions(value = "appManage:appControl:READ")
    public Result whiteList(TbSysAppStoreQry query, @PageableDefault Pageable pageable) {
        Result result = new Result(Result.Status.ERROR,"查询失败");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        Integer role = loginUser.getType();
        if(role != 1){//一般角色
            query.setUid(loginUser.getId());

        }
        logger.info("queryPageList,{}\n{}",query,pageable);
        try {
            Page<TbSysAppStore> page = appService.queryPageWhiteList(query, pageable);
            result.setStatus(Result.Status.OK);
            result.setMessage(page);
        } catch (Exception e) {
            logger.error("UserController-list||sysError:",e);
            result.addError(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/whiteList/delete/{sysAppId}", method = RequestMethod.GET)
    //@RequiresPermissions(value = "appManage:appControl:CREATE")
    public String whiteListDelete(@PathVariable("sysAppId") Integer appId) {
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        int result = appService.deleteSysAppInfo(appId,loginUser.getId());
        return "redirect:/app/whitelist";

    }

    @RequestMapping(value = "/whiteList/{appId}", method = RequestMethod.GET)
    //@RequiresPermissions(value = "appManage:appControl:READ")
    public ModelAndView querySysAppById(@PathVariable("appId") Integer appId) {
        ModelAndView mav = new ModelAndView("app/sysAppEdit");
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        TbSysAppStore vo = appService.querySysAppById(appId);
        mav.addObject("app",vo);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/whiteList",method = RequestMethod.POST)
    //@RequiresPermissions(value = "appManage:appControl:CREATE")
    public Result whiteListAddOne(TbSysAppStore tbAppStore,HttpServletRequest req) {
        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            tbAppStore.setUid(loginUser.getId());
            appService.insertSysApp(tbAppStore);
        } catch (Exception e) {
            logger.error("失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败,"+e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "添加白名单："+result.getStatus(), JSON.toJSONString(tbAppStore),ServletUtils.getClientIpAddress(req));
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/whiteList",method = RequestMethod.PUT)
    //@RequiresPermissions(value = "appManage:appControl:UPDATE")
    public Result whiteListditOne(TbSysAppStore tbAppStore,Integer[] roleIds,HttpServletRequest req) {

        Result result = new Result(Result.Status.OK,"操作成功");
        Subject subject = SecurityUtils.getSubject();
        TbUser loginUser = (TbUser) subject.getSession().getAttribute("userInfo");
        try {
            appService.updateSysApp(tbAppStore);
        } catch (Exception e) {
            logger.error("失败",e);
            result.setStatus(Result.Status.ERROR);
            result.setMessage("操作失败,"+e.getMessage());
        }
        finally {
            tbOperatorLogService.insertLog(loginUser.getName(), "更新白名单："+result.getStatus(), JSON.toJSONString(tbAppStore), ServletUtils.getClientIpAddress(req));
        }
        return result;
    }



}
