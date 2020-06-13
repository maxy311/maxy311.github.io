package com.example.tools;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReadString {
    public static void main(String[] args) {
        String path = "/Users/maxy/Desktop/country.xls";
        ReadString poi = new ReadString();
        poi.loadExcel(path);
        poi.init();

        File shareitFile = new File("/Users/maxy/Desktop/SHAREit/BaseCore");
        poi.writeToFile(shareitFile);
    }


    private Sheet sheet;    //表格类实例
    LinkedList<String>[] result;    //保存每个单元格的数据 ，使用的是一种链表数组的结构

    //读取excel文件，创建表格实例
    private void loadExcel(String filePath) {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(new File(filePath));
            Workbook workBook = WorkbookFactory.create(inStream);

            sheet = workBook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //初始化表格中的每一行，并得到每一个单元格的值
    public void init() {
        int rowNum = sheet.getLastRowNum() + 1;
        result = new LinkedList[rowNum];
        for (int i = 0; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            //每有新的一行，创建一个新的LinkedList对象
            result[i] = new LinkedList();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                //获取单元格的值
                String str = getCellValue(cell);
                //将得到的值放入链表中
                result[i].add(str);
            }
        }
    }

    //获取单元格的值
    private String getCellValue(Cell cell) {
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


    private void writeToFile(File shareitFile) {
        if (!shareitFile.exists())
            throw new RuntimeException("Error ::: " + shareitFile.getAbsolutePath());
        LinkedList<String> dirList = result[0];
        int itemSize = dirList.size();
        for (String dir : dirList) {
            File dirFile = new File(shareitFile, "values-" + dir);
            if (!dirFile.exists())
                dirFile.mkdir();
        }

        LinkedList<String>[] cellList = new LinkedList[itemSize];
        for (int i = 0; i < result.length; i++) {
            LinkedList<String> linkedList = result[i];
            if (linkedList.size() < itemSize) {
                int i1 = itemSize - linkedList.size();
                for (int j = 0; j < i1; j++) {
                    linkedList.add("");
                }
            }
            System.out.println(linkedList.size() + "     " + linkedList);

            for (int j = 0; j < cellList.length; j++) {
                if (cellList[j] == null)
                    cellList[j] = new LinkedList<>();
                cellList[j].add(linkedList.get(j));
            }
        }

        System.out.println("----------------");
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < cellList.length; i++) {
            LinkedList<String> linkedList = cellList[i];
            System.out.println(linkedList.size() + "     " + linkedList);
            if (i == 0) {
                for (int i1 = 1; i1 < linkedList.size(); i1++) {
                    addKeys(keys, linkedList.get(i1));
                }
            }

            String dir = linkedList.get(0);
            File file = createFile(shareitFile, dir);
            System.out.println(file.getAbsolutePath());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
                writeHeader(bw);
                for (int i1 = 1; i1 < linkedList.size(); i1++) {
                    String value = linkedList.get(i1);
                    if (value == null || value == "" || value.length() == 0)
                        continue;
                    String key = keys.get(i1 - 1);
                    key = String.format(key, value);
                    if (key.contains("video_channel_title_action"))
                        System.out.println(key + "         " + value);
                    bw.write(key);
                    bw.flush();
                    bw.newLine();
                }
                writeFooter(bw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void writeHeader(BufferedWriter bw) throws IOException {
        bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<resources>");
        bw.flush();
        bw.newLine();
    }

    private void writeFooter(BufferedWriter bw) throws IOException {
        bw.write("</resources>");
        bw.flush();
    }

    String str = "    <string name=\"video_channel_title_%s\"></string>";
    private void addKeys(List<String> keys, String value) {
        value = value.trim().toLowerCase();
        while (value.contains(" ")) {
            value = value.replace(" ", "");
        }
        value = String.format(str, value);
        value = value.replace("><", ">%s<");
        keys.add(value);
    }

    private File createFile(File shareitFile, String dir) {
        File dirFile = new File(shareitFile, "values-" + dir);
        if (!dirFile.exists())
            throw new RuntimeException("Error " + dirFile.getAbsolutePath());

        File valueFile = new File(dirFile, "video_strings.xml");
        try {
            if (!valueFile.exists())
                valueFile.createNewFile();
        } catch (Exception e) {
        }
        return valueFile;
    }
}
