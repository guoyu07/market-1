package com.appmarket.market.utils;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Admin on 2015/4/23.
 */
public class ViewExcel extends AbstractExcelView {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat df = new DecimalFormat("#.00");


    @Override
    public void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        String[] headerName = (String[]) model.get("headerName");
        String[] headerKey = (String[]) model.get("headerKey");
        String fileName = (String) model.get("fileName");
        List<Object> dataList = (List<Object>) model.get("dataList");
        String[] dataFormat = (String[]) model.get("dataFormat");

        CellStyle headCs = workbook.createCellStyle();
        CellStyle strTypeCs = workbook.createCellStyle();
        CellStyle intCs = workbook.createCellStyle();
        CellStyle doubleType1Cs = workbook.createCellStyle();
        CellStyle doubleType2Cs = workbook.createCellStyle();

        // 创建两种字体
        Font f = workbook.createFont();
        Font f2 = workbook.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        headCs.setFont(f);
        headCs.setBorderLeft(CellStyle.BORDER_THIN);
        headCs.setBorderRight(CellStyle.BORDER_THIN);
        headCs.setBorderTop(CellStyle.BORDER_THIN);
        headCs.setBorderBottom(CellStyle.BORDER_THIN);
        headCs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        strTypeCs.setFont(f2);
        strTypeCs.setBorderLeft(CellStyle.BORDER_THIN);
        strTypeCs.setBorderRight(CellStyle.BORDER_THIN);
        strTypeCs.setBorderTop(CellStyle.BORDER_THIN);
        strTypeCs.setBorderBottom(CellStyle.BORDER_THIN);
        strTypeCs.setAlignment(CellStyle.ALIGN_CENTER);
        strTypeCs.setWrapText(true);

        //整数
        intCs.setFont(f2);
        intCs.setBorderLeft(CellStyle.BORDER_THIN);
        intCs.setBorderRight(CellStyle.BORDER_THIN);
        intCs.setBorderTop(CellStyle.BORDER_THIN);
        intCs.setBorderBottom(CellStyle.BORDER_THIN);
        intCs.setAlignment(CellStyle.ALIGN_CENTER);
        intCs.setWrapText(true);
        intCs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

        //保留1位小数
        doubleType1Cs.setFont(f2);
        doubleType1Cs.setBorderLeft(CellStyle.BORDER_THIN);
        doubleType1Cs.setBorderRight(CellStyle.BORDER_THIN);
        doubleType1Cs.setBorderTop(CellStyle.BORDER_THIN);
        doubleType1Cs.setBorderBottom(CellStyle.BORDER_THIN);
        doubleType1Cs.setAlignment(CellStyle.ALIGN_CENTER);
        doubleType1Cs.setWrapText(true);
        doubleType1Cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));

        //保留2位小数
        doubleType2Cs.setFont(f2);
        doubleType2Cs.setBorderLeft(CellStyle.BORDER_THIN);
        doubleType2Cs.setBorderRight(CellStyle.BORDER_THIN);
        doubleType2Cs.setBorderTop(CellStyle.BORDER_THIN);
        doubleType2Cs.setBorderBottom(CellStyle.BORDER_THIN);
        doubleType2Cs.setAlignment(CellStyle.ALIGN_CENTER);
        doubleType2Cs.setWrapText(true);
        doubleType2Cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));


        HSSFSheet sheet = workbook.createSheet("data");

        // 创建第一行
        Row row = sheet.createRow(0);
        for (int i = 0; i < headerName.length; i++) {
            sheet.setColumnWidth(i, 150 * 35);
            Cell cell = row.createCell(i);
            cell.setCellValue(headerName[i]);
            cell.setCellStyle(headCs);
        }
        String methodName = null;
        //设置每行每列的值
        for (int i = 0; i < dataList.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            HSSFRow sheetRow = sheet.createRow(i + 1);
            Object obj = dataList.get(i);
            Class tCls = obj.getClass();
            // 在row行上创建一个方格
            for (int j = 0; j < headerKey.length; j++) {
                Object value = null;
                if (obj instanceof Map) {
                    value = ((Map) obj).get(headerKey[j]);
                } else {
                    methodName = "get" + (headerKey[j].substring(0, 1)).toUpperCase() + (headerKey[j].substring(1));
                    Method getMethod = tCls.getMethod(methodName, new Class[]{});
                    value = getMethod.invoke(obj, new Object[]{});
                }

                Cell cell = sheetRow.createCell(j);
                String strValue = getCommonValue(value);
                try {
                    if (dataFormat == null || dataFormat.length == 0) {
                        cell.setCellValue(strValue);
                        cell.setCellStyle(strTypeCs);
                    } else {
                        String dataFormatTmp = dataFormat[j];
                        if ("".equals(dataFormatTmp) || "str".equals(dataFormatTmp) || "date".equals(dataFormatTmp)) {
                            cell.setCellValue(strValue);
                            cell.setCellStyle(strTypeCs);
                        } else if ("int".equals(dataFormatTmp)) {
                            cell.setCellValue(Integer.parseInt(strValue));
                            cell.setCellStyle(intCs);
                        } else if ("double1".equals(dataFormatTmp)) {
                            cell.setCellValue(Double.parseDouble(strValue));
                            cell.setCellStyle(doubleType1Cs);
                        } else if ("double2".equals(dataFormatTmp)) {
                            cell.setCellValue(Double.parseDouble(strValue));
                            cell.setCellStyle(doubleType2Cs);
                        } else if ("percent".equals(dataFormatTmp)) {
                            cell.setCellValue(Double.parseDouble(strValue));
                            cell.setCellStyle(doubleType2Cs);
                        }
                    }
                } catch (Exception e) {
                    cell.setCellValue(strValue);
                    cell.setCellStyle(strTypeCs);
                }


            }
        }

        // 设置response参数，可以打开下载页面
        response.reset();
        Boolean needZipFile= false;
        if(model.get("needZipFile")!=null)
            needZipFile =(Boolean) model.get("needZipFile");
        if(needZipFile){
            ZipOutputStream zipStream = new ZipOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");
            ZipEntry entry = new ZipEntry(fileName+".xls");
            zipStream.putNextEntry(entry);
            workbook.write(zipStream);
            zipStream.flush();
            zipStream.close();
        }else{
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        }

    }


    private String getCommonValue(Object value) {
        String textValue = "";
        if (value != null) {
            if (value instanceof Date) {
                textValue = sdf.format((Date) value);
            } else {
                textValue = value.toString();
            }
        } else {
            textValue = " ";
        }
        return textValue;
    }

}