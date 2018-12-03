package com.appmarket.market.utils;


import com.appmarket.market.bean.SnKey;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/23.
 * Email:chenjingxiong@yunnex.com.
 */
public class SnXlsView extends AbstractXlsView {
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        // change the file name
        //response.setCharacterEncoding("UTF-8");
        //response.setHeader("Content-Disposition", "attachment;filename=\""+ URLEncoder.encode("终端密钥", "UTF-8") +".xls\"");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String nowDate = simpleDateFormat.format(date);
        String excelName="终端密钥"+nowDate+".xls";
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(excelName.getBytes("gbk"),"ISO-8859-1"));
        @SuppressWarnings("unchecked")
        List<SnKey> snKeyList = (List<SnKey>) model.get("snKeyList");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("终端密钥信息");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("机身号");
        header.createCell(1).setCellValue("序列号");
        header.createCell(2).setCellValue("密钥");

        // Create data cells
        int rowCount = 1;
        if(!CollectionUtils.isEmpty(snKeyList)){
            for (SnKey snKey : snKeyList){
                Row courseRow = sheet.createRow(rowCount++);
                courseRow.createCell(0).setCellValue(snKey.getSn());
                courseRow.createCell(1).setCellValue(snKey.getSnum());
                courseRow.createCell(2).setCellValue(snKey.getKey());
            }
        }
        else {
            Row courseRow = sheet.createRow(1);
            courseRow.createCell(0).setCellValue("无");
            courseRow.createCell(1).setCellValue("没有设置");
            courseRow.createCell(2).setCellValue("没有设置");
        }

    }
}
