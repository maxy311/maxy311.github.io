package com.wutian.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    public static List<List<String>> readExcel(String xlsPath) {
        return readExcel(xlsPath, null);
    }

    public static List<List<String>> readExcel(String xlsPath, String sheetName) {
        List<List<String>> list = new ArrayList<>();
        File file = new File(xlsPath);
        if (!file.exists()) {
            System.out.println("File not exit::::" + file.getAbsolutePath());
            return list;
        }
        InputStream is = null;
        try {
            // 创建输入流，读取Excel
            is = new FileInputStream(file);
            Workbook workBook = WorkbookFactory.create(is);
            Sheet sheet = null;
            if (sheetName == null || "".equals(sheetName)) {
                sheet = workBook.getSheetAt(0);
            } else {
                sheet = workBook.getSheet(sheetName);
            }
            if (sheet == null) {
                System.out.println("get sheet is empty : " + file.getAbsolutePath());
                return list;
            }

            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                short lastCellNum = row.getLastCellNum();
                List<String> cellList = new ArrayList<>();
                for (int j = 0; j < lastCellNum; j++) {
                    Cell cell = row.getCell(j);
                    //String cellValue = getCellValue(cell);
                    cellList.add(cell.getStringCellValue());
                }
                list.add(cellList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return list;
    }

    //获取单元格的值
    private static String getCellValue(Cell cell) {
        String cellValue = "";
        DataFormatter formatter = new DataFormatter();
        if (cell != null) {
            //判断单元格数据的类型，不同类型调用不同的方法
            switch (cell.getCellType()) {
                //数值类型
                case Cell.CELL_TYPE_NUMERIC:
                    //进一步判断 ，单元格格式是日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = formatter.formatCellValue(cell);
                    } else {
                        //数值
                        double value = cell.getNumericCellValue();
                        int intValue = (int) value;
                        cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                //判断单元格是公式格式，需要做一种特殊处理来得到相应的值
                case Cell.CELL_TYPE_FORMULA: {
                    try {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf(cell.getRichStringCellValue());
                    }

                }
                break;
                case Cell.CELL_TYPE_BLANK:
                    cellValue = "";
                    break;
                case Cell.CELL_TYPE_ERROR:
                    cellValue = "";
                    break;
                default:
                    cellValue = cell.toString().trim();
                    break;
            }
        }
        return cellValue.trim();
    }









    //ExcelUtils.writeExcel(list, "/Users/maxy/Desktop/Workbook1.xls");
    public static void writeExcel(List<List<String>> dataList, String xlsPath) {
        OutputStream out = null;
        try {
            // 获取总列数
            int columnNumCount = dataList.size();

            // 读取Excel文档
            File xlsFile = new File(xlsPath);
            Workbook workBook = getWorkbook(xlsFile);

            // sheet 对应一个工作页
            Sheet sheet = workBook.createSheet();
            /**
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(xlsPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */

            for (int i = 0; i < dataList.size(); i++) {
                Row row = sheet.createRow(i);
                List<String> cellList = dataList.get(i);
                for (int i1 = 0; i1 < cellList.size(); i1++) {
                    Cell first = row.createCell(i1);
                    first.setCellValue(cellList.get(i1));
                }
            }

            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(xlsPath);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(File newFile) throws Exception {
        Workbook wb;
        if (newFile.exists()) {
            // Load existing
            wb = WorkbookFactory.create(newFile);
        } else {
            // What kind of file are they trying to ask for?
            // Add additional supported types here
            if (newFile.getName().endsWith(".xls")) {
                wb = new HSSFWorkbook();
            } else if (newFile.getName().endsWith(".xlsx")) {
                wb = new XSSFWorkbook();
            } else {
                throw new IllegalArgumentException("I don't know how to create that kind of new file");
            }
        }
        return wb;
    }
}
