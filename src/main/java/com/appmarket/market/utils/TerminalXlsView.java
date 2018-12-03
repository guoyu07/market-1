package com.appmarket.market.utils;

import com.appmarket.market.bean.TbTerminal;
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
 * Created by kingson.chan on 2017/4/17.
 * Email:chenjingxiong@yunnex.com.
 */
public class TerminalXlsView extends AbstractXlsView {
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment;filename=\""+ URLEncoder.encode("终端资料", "UTF-8") +".xls\"");
        @SuppressWarnings("unchecked")
        List<TbTerminal> tbTerminalList = (List<TbTerminal>) model.get("tbTerminal");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("终端信息");

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("imei号");
        header.createCell(1).setCellValue("机身号");
        header.createCell(2).setCellValue("机构号");
        header.createCell(3).setCellValue("商铺名称");
        header.createCell(4).setCellValue("商铺联系人");
        header.createCell(5).setCellValue("商铺联系方式");

        // Create data cells
        int rowCount = 1;
        for (TbTerminal tbTerminal : tbTerminalList){
            Row courseRow = sheet.createRow(rowCount++);
            courseRow.createCell(0).setCellValue(tbTerminal.getImei());
            courseRow.createCell(1).setCellValue(tbTerminal.getSn());
            courseRow.createCell(2).setCellValue(tbTerminal.getUid());
            courseRow.createCell(3).setCellValue(tbTerminal.getShopName());
            courseRow.createCell(4).setCellValue(tbTerminal.getShopContacts());
            courseRow.createCell(5).setCellValue(tbTerminal.getShopPhone());
        }
    }

}
