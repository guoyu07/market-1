package com.appmarket.market.utils;


import com.appmarket.market.bean.TbUser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Kingson.chan on 2017/2/23.
 * Email:chenjingxiong@yunnex.com.
 */
public class XlsView extends AbstractXlsView {
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment;filename=\""+ URLEncoder.encode("用户资料", "UTF-8") +".xls\"");
        @SuppressWarnings("unchecked")
        List<TbUser> tbUserList = (List<TbUser>) model.get("tbUser");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("用户基本信息");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("账号");
        header.createCell(1).setCellValue("姓名");
        header.createCell(2).setCellValue("状态");

        // Create data cells
        int rowCount = 1;
        for (TbUser tbUser : tbUserList){
            Row courseRow = sheet.createRow(rowCount++);
            courseRow.createCell(0).setCellValue(tbUser.getId());
            courseRow.createCell(1).setCellValue(tbUser.getName());
            courseRow.createCell(2).setCellValue(tbUser.getStatus());
        }
    }
}
