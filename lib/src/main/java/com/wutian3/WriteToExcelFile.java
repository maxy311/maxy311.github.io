package com.wutian3;

import com.Constants;
import com.wutian.utils.ExcelUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteToExcelFile {
    private static final String EXCEL_NAME = "SHAREit_Translate.xls";
    private static final String TRANSLATE_PATH = "/Users/maxiaoyu/Desktop/SHAREit_Translate";

    public static void main(String[] args) {
        File file = new File(TRANSLATE_PATH);
        if (!file.exists())
            throw new RuntimeException(file.getAbsolutePath() + " not Exist!!!");

        File valueDir = new File(TRANSLATE_PATH, "values");
        File valueZhDir = new File(TRANSLATE_PATH, "values-zh-rCN");

        Map<String, Map<String, Map<String, List<String>>>> dataMap = new LinkedHashMap<>();
        for (File valueFile : valueDir.listFiles()) {
            if (valueFile.isHidden())
                continue;
            if (isReleasenoteFile(valueFile))
                continue;
            File valueZhFile = new File(valueZhDir, valueFile.getName());
            System.out.println(valueZhFile.getAbsolutePath());
            Map<String, Map<String, List<String>>> map = getWriteExcelData(valueFile, valueZhFile);
            dataMap.put(valueFile.getName(), map);
        }

        System.out.println(dataMap);
        writeToExcel(dataMap);
    }

    private static void writeToExcel(Map<String, Map<String, Map<String, List<String>>>> dataMap) {
        OutputStream out = null;
        try {
            File excelFile = new File(Constants.DESKTOP_PATH, EXCEL_NAME);
            Workbook workbook = ExcelUtils.getWorkbook(excelFile);
            String xlsPath = excelFile.getAbsolutePath();
            if (workbook == null)
                throw new RuntimeException("Create workbook Error!   " + xlsPath);

            // sheet 对应一个工作页
            Sheet sheet = workbook.createSheet();
            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }

            out = new FileOutputStream(xlsPath);
            workbook.write(out);

            Set<String> fileNameSet = dataMap.keySet();
            int index = 0;
            for (String fileNameKey : fileNameSet) {
                addSheetRowName(sheet, index, fileNameKey);
                index++;

                Map<String, Map<String, List<String>>> stringMapMap = dataMap.get(fileNameKey);
                for (Map.Entry<String, Map<String, List<String>>> stringMapEntry : stringMapMap.entrySet()) {
                    String fileSubName = stringMapEntry.getKey();
                    addSheetRowName(sheet, index, fileSubName);
                    index++;

                    Map<String, List<String>> value = stringMapEntry.getValue();
                    for (Map.Entry<String, List<String>> listEntry : value.entrySet()) {
                        List<String> listValue = listEntry.getValue();
                        Row row = sheet.createRow(index);
                        for (int i = 0; i < listValue.size(); i++) {
                            Cell cell = row.createCell(i);
                            cell.setCellValue(listValue.get(i));
                        }
                        index++;
                    }

                    addSpaceLine(sheet, index);
                    index++;
                }
                for (int i = 0; i < 3; i++) {
                    addSpaceLine(sheet, index);
                    index++;
                }
            }



            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(xlsPath);
            workbook.write(out);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    private static void addSpaceLine(Sheet sheet, int index) {
        Row row = sheet.createRow(index);
        row.createCell(0);
    }

    private static void addSheetRowName(Sheet sheet, int index, String value) {
        Row row = sheet.createRow(index);
        Cell cell = row.createCell(0);
        cell.setCellValue(value);
    }

    private static Map<String, Map<String, List<String>>> getWriteExcelData(File valueFile, File valueZhFile) {
        Map<String, Map<String, String>> valueMap = readStringToMap(valueFile);
        Map<String, Map<String, String>> valueZhMap = readStringToMap(valueZhFile);
        Map<String, Map<String, List<String>>> map = new LinkedHashMap<>();
        Set<String> keySet = valueMap.keySet();
        for (String key : keySet) {
            //key file_name;
            Map<String, List<String>> keyValueMap = new LinkedHashMap<>();
            map.put(key, keyValueMap);

            Map<String, String> keyValue = valueMap.get(key);
            Map<String, String> zhKeyValue = valueZhMap.get(key);
            for (Map.Entry<String, String> entry : keyValue.entrySet()) {
                String entryKey = entry.getKey();
                String entryValue = entry.getValue();
                String zhEntryValue = zhKeyValue != null ? zhKeyValue.get(entryKey) : "";
                List<String> list = new ArrayList<>();
                list.add(zhEntryValue);
                list.add(entryValue);
                keyValueMap.put(entryKey, list);
            }
        }
        return map;
    }

    private static Map<String, Map<String, String>> readStringToMap(File file) {
        Map<String, Map<String, String>> map = new LinkedHashMap<>();
        if (!file.exists())
            return map;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Map<String, String> values = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.contains("<?xml version=\"1.0\" encoding=\"utf-8\"?>") || line.contains("resources>"))
                    continue;

                if (line.contains("//-----------------------------")) {
                    String fileName = line.trim().replace("//-----------------------------", "");
                    values = new LinkedHashMap<>();
                    map.put(fileName, values);
                    continue;
                }

                if (line == null || line.equals(""))
                    continue;
                if (values == null)
                    throw new RuntimeException("Create Map is Empty");

                if (line.length() == 0)
                    continue;

                String[] split = line.split("\">");
                values.put(split[0], line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static boolean isReleasenoteFile(File file) {
        String name = file.getName();
        return name.toLowerCase().contains("release");
    }
}
