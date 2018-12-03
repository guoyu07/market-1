package com.appmarket.market.web.controller.admin;


import com.appmarket.market.bean.TbUser;
import com.appmarket.market.entity.response.Result;
import com.appmarket.market.service.TbOperatorLogService;
import com.appmarket.market.utils.PropertiesUtil;
import com.appmarket.market.utils.ServletUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/fileUpload")
public class AjaxFileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(AjaxFileUploadController.class);

    private String tmpFilePath = PropertiesUtil.getEntryValue("tmp.pic.path");
    private long fileMaxSize = PropertiesUtil.getLongEntryValue("user.pic.maxSize");
    private String apkFilePath = PropertiesUtil.getEntryValue("tmp.apk.path");
    private String pngFilePath = PropertiesUtil.getEntryValue("tmp.png.path");
    private String apkFileAddress = PropertiesUtil.getEntryValue("file.apk.address");
    private String pngFileAddress = PropertiesUtil.getEntryValue("file.png.address");

    /**
     * 允许上传的扩展名
     */
    private static Map<String,String[]> permitMap = new HashMap<String,String[]>();
    static {
        permitMap.put("png",new String[]{"png"});
        permitMap.put("apk",new String[]{"apk"});
        permitMap.put("userFile",new String[]{"xls"});
        permitMap.put("terminalFile",new String[]{"xls"});
        permitMap.put("appFile",new String[]{"xls"});
    }
    @Autowired
    private TbOperatorLogService userOperatorLogService;

    @ResponseBody
    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST)
    public Result uploadImg(HttpServletRequest req) {
        Result result = upload(req, "png");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/uploadApk",method = RequestMethod.POST)
    public Result uploadApk(HttpServletRequest req) {
        Result result = upload(req, "apk");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{fileType}", method = RequestMethod.POST)
    public Result uploadFile(HttpServletRequest req, @PathVariable String fileType) {
        Result result = upload(req, fileType);
        return result;
    }

    public Result upload(HttpServletRequest req, String type) {
        Result result = new Result(Result.Status.ERROR, "上传失败");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) req;
        TbUser loginUser = (TbUser) SecurityUtils.getSubject().getSession().getAttribute("userInfo");

        if (!multipartRequest.getFileNames().hasNext()) {
            result.addError("请选择需要上传的文件");
            return result;
        }
        String logMemo = null;
        String[] extensionPermit = null;

        if(!permitMap.containsKey(type)){
            result.addError("没有对应的上传类型");
            return result;
        }
        extensionPermit = permitMap.get(type);

        try {
            MultipartFile mulfile = multipartRequest.getFile(multipartRequest.getFileNames().next());
            if (mulfile.getSize() > fileMaxSize) {
                result.addError("上传文件太大.");
                return result;
            }
            String originalFilename = mulfile.getOriginalFilename();
            String extName = FilenameUtils.getExtension(originalFilename);
            String newFileName = System.currentTimeMillis() + "." + extName;
            String realPath = "";
            if("apk".equals(type)){
                realPath =  apkFilePath + loginUser.getName();
            }
            else if("png".equals(type)){
                realPath =  pngFilePath + loginUser.getName();
            }
            else {
                realPath =  req.getSession().getServletContext().getRealPath("") + tmpFilePath + loginUser.getName();
            }

            if (!ArrayUtils.contains(extensionPermit, extName)) {
                result.addError("只允许上传" + ArrayUtils.toString(extensionPermit) + "类型文件");
                return result;
            }

            File tmpFile = new File(realPath, newFileName);
            FileUtils.copyInputStreamToFile(mulfile.getInputStream(), tmpFile);
            if ("png".equals(type)) {
                result.addOK(pngFileAddress + loginUser.getName() + "/" + newFileName);
            }
            else if("apk".equals(type)){
                result.addOK(apkFileAddress + loginUser.getName() + "/"  + newFileName);
            }
            else{
                result.addOK(newFileName);
            }
            //result.addOK(newFileName);
            logMemo = String.format("上传文件日志;\n原文件名=%s;\n新文件名=%s;\nrealPath=%s;", originalFilename, newFileName, realPath);
            userOperatorLogService.insertLog(loginUser.getName(), "上传文件",logMemo, ServletUtils.getClientIpAddress(req));
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            result.addError("上传文件失败");
        } finally {
            logger.info(logMemo);
        }
        return result;
    }

}
